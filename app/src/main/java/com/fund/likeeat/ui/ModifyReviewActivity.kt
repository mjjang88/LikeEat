package com.fund.likeeat.ui

import android.os.Bundle
import android.widget.Toast
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
import com.fund.likeeat.network.LikeEatRetrofit
import com.fund.likeeat.network.PlaceServer
import com.fund.likeeat.network.RetrofitProcedure
import com.fund.likeeat.network.ReviewServerWrite
import com.fund.likeeat.utilities.INTENT_KEY_REVIEW
import com.fund.likeeat.utilities.RESULT_CODE_FINISH_SET_REVIEW
import com.fund.likeeat.utilities.VISIT_DATE_EMPTY_VALUE
import com.fund.likeeat.viewmodels.AddReviewViewModel
import com.fund.likeeat.widget.CategorySelectBottomSheetFragment
import com.fund.likeeat.widget.ThemeSelectBottomSheetFragment
import kotlinx.android.synthetic.main.activity_add_review.*
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ModifyReviewActivity : AppCompatActivity() {

    private val addReviewViewModel: AddReviewViewModel by viewModel { parametersOf(MyApplication.pref.uid) }

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

        val review = if (reviews == null) {
            intent.getParcelableExtra(INTENT_KEY_REVIEW)
        } else {
            reviews[0]
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

    private fun initComponent(binding: ActivityModifyReviewBinding) {
        binding.btnCategory.setOnClickListener {
            val categoryBottomSheetFragment = CategorySelectBottomSheetFragment()
            categoryBottomSheetFragment.addReviewViewModel = addReviewViewModel
            categoryBottomSheetFragment.show(supportFragmentManager, categoryBottomSheetFragment.tag)
        }

        if (binding.checkIsPublic.isChecked) {
            binding.checkIsPublic.text = getString(R.string.share)
        } else {
            binding.checkIsPublic.text = getString(R.string.not_share)
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

        binding.btnOk.setOnClickListener {
            doSetReview()
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

    private fun doSetReview() {

        val reviewList: ArrayList<Review> = ArrayList()
        val reviews = intent.getParcelableArrayListExtra<Review>(INTENT_KEY_REVIEW)

        if (reviews == null) {
            val review: Review? = intent.getParcelableExtra(INTENT_KEY_REVIEW)
            review?.let{
                reviewList.add(it)
            }
        } else {
            reviewList.addAll(reviews)
        }

        val reviewWrite = setReviewNullToBlack(addReviewViewModel.editedReview.value)
        reviewWrite.isPublic = check_is_public.isChecked

        var bSendUserInfoSuccess = false
        GlobalScope.launch(Dispatchers.Default) {
            try {
                reviewList.forEach {
                    LikeEatRetrofit.getService().setReview(it.id, reviewWrite).apply {
                        if (isSuccessful) {
                            bSendUserInfoSuccess = true
                        }
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        lifecycleScope.launch(Dispatchers.Default) {
            while (!bSendUserInfoSuccess) {
                delay(1000)
            }

            withContext(Dispatchers.Main) {
                Toast.makeText(this@ModifyReviewActivity, "리뷰 수정 완료", Toast.LENGTH_LONG).show()
                RetrofitProcedure.getUserReview(MyApplication.pref.uid)

                val review = reviewList[0].copy(category = reviewWrite.category, isPublic = reviewWrite.isPublic)

                intent.putExtra(INTENT_KEY_REVIEW, review)
                setResult(RESULT_CODE_FINISH_SET_REVIEW, intent)
                finish()
            }
        }
    }

    private fun setReviewNullToBlack(review: ReviewServerWrite?): ReviewServerWrite {
        return  ReviewServerWrite(
            review?.isPublic?: false,
            review?.category?: "",
            review?.comment?: "",
            review?.visitedDayYmd?: VISIT_DATE_EMPTY_VALUE,
            review?.companions?: "",
            review?.toliets?: "",
            review?.priceRange?: "",
            review?.serviceQuality?: "",
            review?.themeIds?: "",
            review?.uid?: -1,
            review?.revisit?: "",
            review?.place
        )
    }
}