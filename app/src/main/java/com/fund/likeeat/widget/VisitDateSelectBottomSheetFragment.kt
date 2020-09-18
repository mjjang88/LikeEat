package com.fund.likeeat.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fund.likeeat.databinding.BottomSheetSelectVisitDateBinding
import com.fund.likeeat.viewmodels.AddReviewViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class VisitDateSelectBottomSheetFragment: BottomSheetDialogFragment() {

    var addReviewViewModel : AddReviewViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = BottomSheetSelectVisitDateBinding.inflate(inflater, container, false)
        context ?: return binding.root

        binding.btnOk.setOnClickListener {
            addReviewViewModel?.let {
                val year = binding.datePickerVisitDate.year
                val month = String.format("%02d", binding.datePickerVisitDate.month + 1)
                val day = binding.datePickerVisitDate.dayOfMonth
                val date = "$year.$month.$day"
                it.setVisitDate(date)
            }
            dismiss()
        }

        return binding.root
    }
}