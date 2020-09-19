package com.fund.likeeat.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fund.likeeat.adapter.AddReviewBottomSheetListAdapter
import com.fund.likeeat.adapter.GridItem
import com.fund.likeeat.databinding.BottomSheetSelectEvaluationBinding
import com.fund.likeeat.manager.Evaluation
import com.fund.likeeat.viewmodels.AddReviewViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EvaluationSelectBottomSheetFragment: BottomSheetDialogFragment() {

    var addReviewViewModel : AddReviewViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = BottomSheetSelectEvaluationBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val adapter = AddReviewBottomSheetListAdapter()
        binding.listEvaluation.adapter = adapter

        val gridItemList: ArrayList<GridItem> = ArrayList()
        Evaluation.values().forEach {
            gridItemList.add(GridItem(it.evalName, it.imageId))
        }
        adapter.submitList(gridItemList)

        addReviewViewModel?.editedReview?.value?.serviceQuality?.let {
            adapter.setSelectItem(it)
        }

        binding.btnOk.setOnClickListener {
            addReviewViewModel?.let {
                val selectedItem = adapter.getSelectedItem()
                it.setEvaluation(selectedItem.name)
            }
            dismiss()
        }

        return binding.root
    }
}