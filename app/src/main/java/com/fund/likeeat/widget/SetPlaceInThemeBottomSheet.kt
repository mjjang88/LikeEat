package com.fund.likeeat.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.fund.likeeat.R
import com.fund.likeeat.data.PlaceWhenChangeReview
import com.fund.likeeat.data.Review
import com.fund.likeeat.data.ReviewChanged
import com.fund.likeeat.databinding.BottomSheetSetPlaceInThemeBinding
import com.fund.likeeat.network.RetrofitProcedure
import com.fund.likeeat.utilities.*
import com.fund.likeeat.viewmodels.OneReviewViewModel
import com.fund.likeeat.viewmodels.ReviewInThemeViewModel
import com.fund.likeeat.viewmodels.ReviewThemeLinkViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import org.koin.androidx.viewmodel.ext.android.viewModel

class SetPlaceInThemeBottomSheet : BottomSheetDialogFragment() {
    var reviewId: Long? = null
    var themeId: Long? = null
    var themesIdString: String? = null
    var x: Double? = null
    var y: Double? = null
    var placeName: String? = null

    var reviewAndThemeDataBundle = Bundle()
    var isBundleFilled = false

    private val oneReviewViewModel: OneReviewViewModel by viewModel { parametersOf(reviewId) }
    private val linkViewModel: ReviewThemeLinkViewModel by inject()
    private val reviewInThemeViewModel : ReviewInThemeViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        reviewId = arguments?.getLong("REVIEW_ID")
        themeId = arguments?.getLong("THEME_ID")
        x = arguments?.getDouble("PLACE_X")
        y = arguments?.getDouble("PLACE_Y")
        placeName = arguments?.getString("PLACE_NAME")

        GlobalScope.launch { linkViewModel.getThemeIdList(reviewId!!) }

        oneReviewViewModel.review.observe(viewLifecycleOwner) {
            reviewAndThemeDataBundle.apply {
                putLong("REVIEW_ID", reviewId!!)
                putLong("THEME_ID", themeId!!)
                putBoolean("REVIEW_IS_PUBLIC", it.isPublic)
                putString("REVIEW_CATEGORY", it.category)
                putString("REVIEW_COMMENT", it.comment)
                putString("REVIEW_VISITED_DAY_YMD", it.visitedDayYmd)
                putString("REVIEW_COMPANIONS", it.companions)
                putString("REVIEW_TOILETS", it.toliets)
                putString("REVIEW_PRICE_RANGE", it.priceRange)
                putString("REVIEW_SERVICE_QUALITY", it.serviceQuality)
                putString("REVIEW_REVISIT", it.revisit)
                putDouble("PLACE_X", it.x?:0.0)
                putDouble("PLACE_Y", it.y?:0.0)
                putString("PLACE_ADDRESS", it.address_name)
                putString("PLACE_NAME", it.place_name)
                putString("PLACE_PHONE_NUMBER", it.phone)
            }
            isBundleFilled = true
        }
        linkViewModel.themeIdList.observe(viewLifecycleOwner) {
            themesIdString = linkViewModel.getThemeIdString()
            reviewAndThemeDataBundle.putString("THEMES_ID_STRING", themesIdString)
        }

        val binding = DataBindingUtil.inflate<BottomSheetSetPlaceInThemeBinding>(
            inflater,
            R.layout.bottom_sheet_set_place_in_theme,
            container,
            false
        ).apply {
            lifecycleOwner = viewLifecycleOwner

            actionMove.setOnClickListener {
                themesIdString?.let {
                    if(isBundleFilled) {
                        val dialog = MoveReviewInThemeBottomSheet()
                        dialog.arguments = reviewAndThemeDataBundle
                        dialog.show(this@SetPlaceInThemeBottomSheet.parentFragmentManager, dialog.tag)
                        dismiss()
                    } else {
                        ToastUtil.toastShort("다시 시도해주세요")
                    }
                }?: ToastUtil.toastShort("다시 시도해주세요")

            }

            actionDelete.setOnClickListener {
                themesIdString?.let {
                    CustomAlertDialog(requireContext())
                        .setTitle(resources.getString(R.string.delete_theme_title))
                        .setMessage(resources.getString(R.string.delete_review_content))
                        .setPositiveButton(resources.getString(R.string.delete_theme_ok)) {
                            GlobalScope.launch {
                                reviewInThemeViewModel.getAllReviews(x ?: NO_X_VALUE, y ?: NO_Y_VALUE, placeName ?: NO_PLACE_NAME)
                            }
                            dismiss()
                        }.setNegativeButton(resources.getString(R.string.delete_theme_cancel)) {
                            dismiss()
                        }.show()
                }?: ToastUtil.toastShort("다시 시도해주세요")
            }
        }

        reviewInThemeViewModel.allReviewsList.observe(viewLifecycleOwner) { result: List<Review> ->
            val reviewChanged = makeReviewChanged(result[0])
            for(review in result) {
                reviewChanged?.let {
                    RetrofitProcedure.updateReviewOnlyTheme(review.id, themeId!!, it, UpdateReviewOnlyThemeType.TYPE_DELETE)
                }?:ToastUtil.toastShort("Error")
            }
            ToastUtil.toastShort("맛집을 테마에서 제거했습니다")
        }
        return binding.root
    }

    private fun makeReviewChanged(review: Review): ReviewChanged? {
        val place = PlaceWhenChangeReview(review.x ?: NO_X_VALUE, review.y ?: NO_Y_VALUE, review.address_name, review.place_name, review.phone)
        val themeIds = deleteThemeIdInListAndReturnToString()
        return ReviewChanged(
            review.isPublic,
            review.category,
            review.comment,
            review.visitedDayYmd,
            review.companions,
            review.toliets,
            review.priceRange,
            review.serviceQuality,
            review.revisit,
            themeIds,
            place
        )
    }

    private fun deleteThemeIdInListAndReturnToString(): String {
        val themesIdList = themesIdString?.split(",")
        val newList = themesIdList!!.filter { it != themeId.toString() }

        val builder = StringBuilder()
        for((index, item) in newList.withIndex()) {
            builder.append(item)
            if(index != newList.size-1) {
                builder.append(",")
            }
        }
        return builder.toString()
    }
}