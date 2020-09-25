package com.fund.likeeat.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.fund.likeeat.R
import com.fund.likeeat.adapter.AddReviewThemeAdapter
import com.fund.likeeat.adapter.ReviewVisitRecordListAdapter
import com.fund.likeeat.data.Review
import com.fund.likeeat.data.Theme
import com.fund.likeeat.databinding.ActivityReviewDetailBinding
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.manager.getCategoryImageByName
import com.fund.likeeat.utilities.INTENT_KEY_REVIEW
import com.fund.likeeat.utilities.INTENT_KEY_REVIEW_CREATE
import com.fund.likeeat.viewmodels.AddReviewViewModel
import com.fund.likeeat.viewmodels.ReviewDetailViewModel
import com.fund.likeeat.widget.CategorySelectBottomSheetFragment
import com.fund.likeeat.widget.ReviewMoreBottomSheetFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ReviewDetailActivity : AppCompatActivity() {

    private val reviewDetailViewModel: ReviewDetailViewModel by viewModel { parametersOf(MyApplication.pref.uid) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityReviewDetailBinding>(
            this,
            R.layout.activity_review_detail
        )

        val review = intent.getParcelableExtra<Review>(INTENT_KEY_REVIEW)
        binding.review = review
        reviewDetailViewModel.inputReview.value = review

        review?.let {
            initComponent(binding, it)
        }
    }

    private fun initComponent(binding: ActivityReviewDetailBinding, review: Review) {

        if (review.category.isNullOrBlank()) {
            val drawable = resources.getDrawable(R.drawable.btn_plus_red, null)
            binding.btnCategory.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnCategory.text = getString(R.string.category)
            binding.btnCategory.setTextColor(getColor(R.color.colorPrimary))
        } else {
            val drawable = resources.getDrawable(getCategoryImageByName(review.category), null)
            binding.btnCategory.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnCategory.text = review.category
            binding.btnCategory.setTextColor(getColor(R.color.colorBlack))
        }

        if (review.isPublic) {
            binding.checkIsPublic.isChecked = true
            binding.checkIsPublic.text = getString(R.string.share)
        } else {
            binding.checkIsPublic.isChecked = false
            binding.checkIsPublic.text = getString(R.string.not_share)
        }

        val themeAdapter = AddReviewThemeAdapter()
        binding.listTheme.adapter = themeAdapter
        themeAdapter.checkable = false
        reviewDetailViewModel.getThemeList(review)
        reviewDetailViewModel.themeList.observe(this) {
            themeAdapter.submitList(it)
        }

        val reviewAdapter = ReviewVisitRecordListAdapter()
        binding.listReview.adapter = reviewAdapter
        reviewDetailViewModel.reviews.observe(this) {
            reviewAdapter.submitList(it)
        }

        binding.editComment.setOnClickListener {
            val intent = Intent(this, ModifyReviewDetailActivity::class.java)
            intent.putExtra(INTENT_KEY_REVIEW, review)
            intent.putExtra(INTENT_KEY_REVIEW_CREATE, true)
            startActivity(intent)
        }

        binding.btnMore.setOnClickListener {
            val reviewMoreBottomSheetFragment = ReviewMoreBottomSheetFragment()
            reviewMoreBottomSheetFragment.arguments = Bundle().apply { putParcelableArray(INTENT_KEY_REVIEW, reviewDetailViewModel.reviews.value?.toTypedArray()) }
            reviewMoreBottomSheetFragment.show(supportFragmentManager, reviewMoreBottomSheetFragment.tag)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}