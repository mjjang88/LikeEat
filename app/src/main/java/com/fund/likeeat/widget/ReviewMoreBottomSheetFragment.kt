package com.fund.likeeat.widget

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fund.likeeat.databinding.BottomSheetReivewMoreBinding
import com.fund.likeeat.ui.AddReviewActivity
import com.fund.likeeat.ui.ModifyReviewAcitivity
import com.fund.likeeat.utilities.INTENT_KEY_PLACE
import com.fund.likeeat.utilities.INTENT_KEY_REVIEW
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.ArrayList

class ReviewMoreBottomSheetFragment: BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = BottomSheetReivewMoreBinding.inflate(inflater, container, false)
        context ?: return binding.root

        initReview(binding)

        return binding.root
    }

    private fun initReview(binding: BottomSheetReivewMoreBinding) {

        binding.layoutEdit.setOnClickListener {
            val review = arguments?.getParcelableArray(INTENT_KEY_REVIEW)

            review?.let {
                val intent = Intent(requireContext(), ModifyReviewAcitivity::class.java)
                intent.putParcelableArrayListExtra(INTENT_KEY_REVIEW,
                    review.toList() as ArrayList<out Parcelable>?
                )
                startActivity(intent)
            }

            dismiss()
        }
    }
}