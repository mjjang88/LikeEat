package com.fund.likeeat.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fund.likeeat.databinding.BottomSheetReivewMoreBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ReviewMoreBottomSheetFragment: BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = BottomSheetReivewMoreBinding.inflate(inflater, container, false)
        context ?: return binding.root

        return binding.root
    }
}