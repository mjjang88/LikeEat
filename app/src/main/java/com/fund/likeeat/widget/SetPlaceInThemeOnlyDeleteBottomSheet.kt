package com.fund.likeeat.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.fund.likeeat.R
import com.fund.likeeat.databinding.BottomSheetSetPlaceInThemeOnlyDeleteBinding
import com.fund.likeeat.network.RetrofitProcedure
import com.fund.likeeat.utilities.NO_PLACE_NAME
import com.fund.likeeat.utilities.NO_X_VALUE
import com.fund.likeeat.utilities.NO_Y_VALUE
import com.fund.likeeat.viewmodels.DeleteReviewViewModel
import com.fund.likeeat.viewmodels.ReviewThemeLinkViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SetPlaceInThemeOnlyDeleteBottomSheet : BottomSheetDialogFragment() {
    var reviewId: Long? = null
    var x: Double? = null
    var y: Double? = null
    var placeName: String? = null

    private val deleteReviewViewModel: DeleteReviewViewModel by inject()
    private val linkViewModel: ReviewThemeLinkViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        reviewId = arguments?.getLong("REVIEW_ID")
        x = arguments?.getDouble("PLACE_X")
        y = arguments?.getDouble("PLACE_Y")
        placeName = arguments?.getString("PLACE_NAME")

        deleteReviewViewModel.reviewsList.observe(viewLifecycleOwner) { reviewList ->
            val themeIds = linkViewModel.themeIdList.value?.map{ it.themeId }!!
            for(i in reviewList) {
                RetrofitProcedure.deleteReview(i.id, themeIds)
            }
        }

        val binding = DataBindingUtil.inflate<BottomSheetSetPlaceInThemeOnlyDeleteBinding>(
            inflater,
            R.layout.bottom_sheet_set_place_in_theme_only_delete,
            container,
            false
        ).apply {
            // viewModel = themeViewModel
            lifecycleOwner = viewLifecycleOwner

            actionDelete.setOnClickListener {

                CustomAlertDialog(requireContext())
                    .setTitle("알림")
                    .setMessage("등록한 맛집을 완전히 삭제합니다.\n계속 하시겠습니까?")
                    .setPositiveButton("삭제") {
                        GlobalScope.launch {
                            linkViewModel.getThemeIdList(reviewId!!)
                            deleteReviewViewModel.getAllReviews(x ?: NO_X_VALUE, y ?: NO_Y_VALUE, placeName ?: NO_PLACE_NAME)
                        }
                        dismiss()
                    }.setNegativeButton("취소") {
                        dismiss()
                    }.show()
            }
        }

        return binding.root
    }
}