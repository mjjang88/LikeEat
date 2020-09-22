package com.fund.likeeat.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.fund.likeeat.R
import com.fund.likeeat.databinding.BottomSheetSetReviewInThemeBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SetReviewInThemeBottomSheet: BottomSheetDialogFragment() {
    var reviewId: Long? = null
    //private val themeViewModel: OneThemeViewModel by viewModel { parametersOf(themeId) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        reviewId = arguments?.getLong("REVIEW_ID")

        val binding = DataBindingUtil.inflate<BottomSheetSetReviewInThemeBinding>(
            inflater,
            R.layout.bottom_sheet_set_review_in_theme,
            container,
            false
        ).apply {
                // viewModel = themeViewModel
                lifecycleOwner = viewLifecycleOwner

                actionMove.setOnClickListener {
                    val bundle = Bundle().apply {
                        putLong("REVIEW_ID", reviewId!!)
                        putLong("THEME_ID", arguments?.getLong("THEME_ID")!!)
                    }

                    val dialog = MoveReviewInThemeBottomSheet()
                    dialog.arguments = bundle
                    dialog.show(this@SetReviewInThemeBottomSheet.parentFragmentManager, dialog.tag)

                    dismiss()
                }

                actionDelete.setOnClickListener {
                    val dialog = DeleteReviewInThemeDialog(requireContext(), reviewId ?: throw Exception())
                    dialog.setCancelable(false)
                    dialog.show()
                    dismiss()
                }

            }
        return binding.root
    }
}