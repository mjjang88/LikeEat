package com.fund.likeeat.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.fund.likeeat.R
import com.fund.likeeat.data.Review
import com.fund.likeeat.databinding.ActivityModifyReviewDetailBinding
import com.fund.likeeat.manager.*
import com.fund.likeeat.network.PlaceServer
import com.fund.likeeat.network.ReviewServerWrite
import com.fund.likeeat.utilities.INTENT_KEY_REVIEW
import com.fund.likeeat.utilities.INTENT_KEY_REVIEW_CREATE
import com.fund.likeeat.viewmodels.AddReviewViewModel
import com.fund.likeeat.widget.*
import kotlinx.android.synthetic.main.activity_add_review.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ModifyReviewDetailActivity : AppCompatActivity() {

    private val addReviewViewModel: AddReviewViewModel by viewModel { parametersOf(MyApplication.pref.uid) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityModifyReviewDetailBinding>(
            this,
            R.layout.activity_modify_review_detail
        )

        initReview(binding)

        initComponent(binding)

        addReviewViewModel.editedReview.observe(this) {
            it?.let { review -> updateReview(binding, review) }
        }
    }

    private fun initReview(binding: ActivityModifyReviewDetailBinding) {
        var review = intent.getParcelableExtra<Review>(INTENT_KEY_REVIEW)

        if (review != null) {
            val isCreateReview = intent.getBooleanExtra(INTENT_KEY_REVIEW_CREATE, false)
            if (isCreateReview) {
                review = Review(review.id, review.uid, review.isPublic, review.category,
                    null, null, null, null, null, null, null, null,
                    review.x, review.y, review.place_name, review.place_name, review.phone)
            }

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
        }
    }

    private fun initComponent(binding: ActivityModifyReviewDetailBinding) {
        binding.btnExtend.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                binding.layoutDetailPage.visibility = View.VISIBLE
            } else {
                binding.layoutDetailPage.visibility = View.GONE
            }
        }

        binding.btnVisitDate.setOnClickListener {
            val visitDateBottomSheetFragment = VisitDateSelectBottomSheetFragment()
            visitDateBottomSheetFragment.addReviewViewModel = addReviewViewModel
            visitDateBottomSheetFragment.show(supportFragmentManager, visitDateBottomSheetFragment.tag)
        }

        binding.btnEvaluation.setOnClickListener {
            val evalBottomSheetFragment = EvaluationSelectBottomSheetFragment()
            evalBottomSheetFragment.addReviewViewModel = addReviewViewModel
            evalBottomSheetFragment.show(supportFragmentManager, evalBottomSheetFragment.tag)
        }

        binding.btnCompanion.setOnClickListener {
            val companionBottomSheetFragment = CompanionSelectBottomSheetFragment()
            companionBottomSheetFragment.addReviewViewModel = addReviewViewModel
            companionBottomSheetFragment.show(supportFragmentManager, companionBottomSheetFragment.tag)
        }

        binding.btnPrice.setOnClickListener {
            val priceBottomSheetFragment = PriceSelectBottomSheetFragment()
            priceBottomSheetFragment.addReviewViewModel = addReviewViewModel
            priceBottomSheetFragment.show(supportFragmentManager, priceBottomSheetFragment.tag)
        }

        binding.btnRestroom.setOnClickListener {
            val toiletBottomSheetFragment = ToiletSelectBottomSheetFragment()
            toiletBottomSheetFragment.addReviewViewModel = addReviewViewModel
            toiletBottomSheetFragment.show(supportFragmentManager, toiletBottomSheetFragment.tag)
        }

        binding.btnReVisit.setOnClickListener {
            val revisitBottomSheetFragment = RevisitSelectBottomSheetFragment()
            revisitBottomSheetFragment.addReviewViewModel = addReviewViewModel
            revisitBottomSheetFragment.show(supportFragmentManager, revisitBottomSheetFragment.tag)
        }

        binding.editComment.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.length?.let {
                    if (it > 0) {
                        binding.editComment.background = getDrawable(R.drawable.edit_background_shape)
                    } else {
                        binding.editComment.background = getDrawable(R.drawable.edit_background_red_shape)
                    }
                }

                setEnableBtnOk()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun updateReview(binding: ActivityModifyReviewDetailBinding, review: ReviewServerWrite) {

        if (review.visitedDayYmd.isNullOrBlank()) {
            val drawable = resources.getDrawable(R.drawable.btn_plus_black, null)
            binding.btnVisitDate.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnVisitDate.text = getString(R.string.visit_date)
        } else {
            val drawable = resources.getDrawable(R.drawable.ic_select_date, null)
            binding.btnVisitDate.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnVisitDate.text = calcVisitDate(review.visitedDayYmd?: "")
        }

        if (review.serviceQuality.isNullOrBlank()) {
            val drawable = resources.getDrawable(R.drawable.btn_plus_black, null)
            binding.btnEvaluation.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnEvaluation.text = getString(R.string.evaluation)
        } else {
            val drawable = resources.getDrawable(getEvaluationImageByName(review.serviceQuality!!), null)
            binding.btnEvaluation.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnEvaluation.text = review.serviceQuality
        }

        if (review.companions.isNullOrBlank()) {
            val drawable = resources.getDrawable(R.drawable.btn_plus_black, null)
            binding.btnCompanion.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnCompanion.text = getString(R.string.companion)
        } else {
            val drawable = resources.getDrawable(getCompanionImageByName(review.companions!!), null)
            binding.btnCompanion.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnCompanion.text = review.companions
        }

        if (review.priceRange.isNullOrBlank()) {
            val drawable = resources.getDrawable(R.drawable.btn_plus_black, null)
            binding.btnPrice.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnPrice.text = getString(R.string.price)
        } else {
            val drawable = resources.getDrawable(getPriceImageByName(review.priceRange!!), null)
            binding.btnPrice.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnPrice.text = review.priceRange
        }

        if (review.toliets.isNullOrBlank()) {
            val drawable = resources.getDrawable(R.drawable.btn_plus_black, null)
            binding.btnRestroom.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnRestroom.text = getString(R.string.restroom)
        } else {
            val drawable = resources.getDrawable(getToiletImageByName(review.toliets!!), null)
            binding.btnRestroom.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnRestroom.text = review.toliets
        }

        if (review.revisit.isNullOrBlank()) {
            val drawable = resources.getDrawable(R.drawable.btn_plus_black, null)
            binding.btnReVisit.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnReVisit.text = getString(R.string.re_visit)
        } else {
            val drawable = resources.getDrawable(getRevisitImageByName(review.revisit!!), null)
            binding.btnReVisit.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            binding.btnReVisit.text = review.revisit
        }

        setEnableBtnOk()
    }

    fun calcVisitDate(date: String): String {
        if (date.length == 8) {
            val year = date.substring(0, 4)
            val month = date.substring(4, 6)
            val day = date.substring(6, 8)

            return "$month.$day"
        }
        return date
    }

    fun setEnableBtnOk() {
        btn_ok.isEnabled = edit_comment.text.length > 0
    }
}