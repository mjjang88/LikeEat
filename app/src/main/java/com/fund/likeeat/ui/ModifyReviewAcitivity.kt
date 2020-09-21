package com.fund.likeeat.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.fund.likeeat.R
import com.fund.likeeat.adapter.AddReviewThemeAdapter
import com.fund.likeeat.data.Review
import com.fund.likeeat.data.Theme
import com.fund.likeeat.databinding.ActivityModifyReviewBinding
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.manager.getCategoryImageByName
import com.fund.likeeat.network.PlaceServer
import com.fund.likeeat.network.ReviewServerWrite
import com.fund.likeeat.utilities.INTENT_KEY_REVIEW
import com.fund.likeeat.viewmodels.AddReviewViewModel
import com.fund.likeeat.widget.CategorySelectBottomSheetFragment
import com.fund.likeeat.widget.ThemeSelectBottomSheetFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ModifyReviewAcitivity : AppCompatActivity() {

    private val addReviewViewModel: AddReviewViewModel by viewModel { parametersOf(MyApplication.pref.uid) }

    lateinit var mReviews: ArrayList<Review>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityModifyReviewBinding>(
            this,
            R.layout.activity_modify_review
        )

        initReview(binding)

        initComponent(binding)

        addReviewViewModel.editedReview.observe(this) {
            it?.let { review -> updateReview(binding, review) }
        }
    }

    private fun initReview(binding: ActivityModifyReviewBinding) {
        val reviews = intent.getParcelableArrayListExtra<Review>(INTENT_KEY_REVIEW)

        reviews?.let {
            val review = it[0]
            binding.review = review

            lifecycleScope.launch {
                val themeIds = addReviewViewModel.getThemeIds(review)

                withContext(Dispatchers.Main) {
                    addReviewViewModel.editedReview.value = ReviewServerWrite(review.isPublic, review.category, review.comment, review.visitedDayYmd, review.companions,
                        review.toliets, review.priceRange, review.serviceQuality, themeIds, review.uid, review.revisit,
                        PlaceServer(review.y?: 0.0, review.x?: 0.0, review.address_name?: "", review.place_name?: "", review.phone?: "")
                    )
                }
            }
            mReviews = it
        }
    }

    private fun initComponent(binding: ActivityModifyReviewBinding) {
        binding.btnCategory.setOnClickListener {
            val categoryBottomSheetFragment = CategorySelectBottomSheetFragment()
            categoryBottomSheetFragment.addReviewViewModel = addReviewViewModel
            categoryBottomSheetFragment.show(supportFragmentManager, categoryBottomSheetFragment.tag)
        }

        binding.checkIsPublic.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.text = getString(R.string.share)
            } else {
                buttonView.text = getString(R.string.not_share)
            }
        }

        binding.layoutAddTheme.setOnClickListener {
            val themeBottomSheetFragment = ThemeSelectBottomSheetFragment()
            themeBottomSheetFragment.addReviewViewModel = addReviewViewModel
            themeBottomSheetFragment.show(supportFragmentManager, themeBottomSheetFragment.tag)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun updateReview(binding: ActivityModifyReviewBinding, review: ReviewServerWrite) {

        if (review.category.isNullOrBlank()) {
            val drawable = resources.getDrawable(R.drawable.btn_plus_red, null)
            binding.btnCategory.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnCategory.text = getString(R.string.category)
            binding.btnCategory.setTextColor(getColor(R.color.colorPrimary))
        } else {
            val drawable = resources.getDrawable(getCategoryImageByName(review.category!!), null)
            binding.btnCategory.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnCategory.text = review.category
            binding.btnCategory.setTextColor(getColor(R.color.colorBlack))
        }

        if (review.themeIds != null) {
            val adapter = AddReviewThemeAdapter()
            binding.listTheme.adapter = adapter
            adapter.checkable = false

            lifecycleScope.launch(Dispatchers.IO) {
                val themeList = addReviewViewModel.getThemeList()
                val themeIds = addReviewViewModel.editedReview.value?.themeIds?.split(",")
                val checkedList: ArrayList<Theme> = ArrayList()

                themeIds?.forEach { themeId ->
                    if (themeId.isNullOrBlank()) return@forEach
                    themeList.find {
                        it.id == themeId.toLong()
                    }.apply {
                        if (this != null) {
                            checkedList.add(this)
                        }
                    }
                }

                withContext(Dispatchers.Main) {
                    adapter.submitList(checkedList)
                }
            }
        }

        setEnableBtnOk()
    }

    fun setEnableBtnOk() {
        !addReviewViewModel.editedReview.value?.category.isNullOrBlank()
    }
}