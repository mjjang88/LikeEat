package com.fund.likeeat.widget

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.fund.likeeat.R
import com.fund.likeeat.databinding.BottomSheetSetReviewInThemeBinding
import com.fund.likeeat.utilities.ToastUtil
import com.fund.likeeat.viewmodels.OneReviewViewModel
import com.fund.likeeat.viewmodels.ReviewThemeLinkViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.core.parameter.parametersOf
import org.koin.androidx.viewmodel.ext.android.viewModel

class SetReviewInThemeBottomSheet : BottomSheetDialogFragment() {
    var reviewId: Long? = null
    var themeId: Long? = null
    var themesIdString: String? = null
    var reviewAndThemeDataBundle = Bundle()
    var isBundleFilled = false

    private val oneReviewViewModel: OneReviewViewModel by viewModel { parametersOf(reviewId) }
    private val linkViewModel: ReviewThemeLinkViewModel by viewModel { parametersOf(reviewId, themeId) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        reviewId = arguments?.getLong("REVIEW_ID")
        themeId = arguments?.getLong("THEME_ID")

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
                Log.i("REVIEW_REVISIT", it.revisit.toString())
                Log.i("REVIEW_SERVICE_QUALITY", it.serviceQuality.toString())
                putDouble("PLACE_LAT", it.x?:0.0)
                putDouble("PLACE_LNG", it.y?:0.0)
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

        val binding = DataBindingUtil.inflate<BottomSheetSetReviewInThemeBinding>(
            inflater,
            R.layout.bottom_sheet_set_review_in_theme,
            container,
            false
        ).apply {
            // viewModel = themeViewModel
            lifecycleOwner = viewLifecycleOwner


            actionMove.setOnClickListener {
                themesIdString?.let {
                    val dialog = MoveReviewInThemeBottomSheet()
                    dialog.arguments = reviewAndThemeDataBundle
                    dialog.show(this@SetReviewInThemeBottomSheet.parentFragmentManager, dialog.tag)
                    dismiss()
                }?: ToastUtil.toastShort("다시 시도해주세요")

            }

            actionDelete.setOnClickListener {
                themesIdString?.let {
                    if (isBundleFilled) {
                        val dialog =
                            DeleteReviewInThemeDialog(requireContext(), reviewAndThemeDataBundle)
                        dialog.setCancelable(false)
                        dialog.show()
                        dismiss()
                    } else {
                        ToastUtil.toastShort("다시 시도해주세요")
                    }
                }?: ToastUtil.toastShort("다시 시도해주세요")
            }
        }
        return binding.root
    }
}