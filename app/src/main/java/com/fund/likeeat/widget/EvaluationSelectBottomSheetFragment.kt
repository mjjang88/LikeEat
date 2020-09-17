package com.fund.likeeat.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fund.likeeat.R
import com.fund.likeeat.adapter.AddReviewBottomSheetListAdapter
import com.fund.likeeat.adapter.GridItem
import com.fund.likeeat.databinding.BottomSheetSelectEvaluationBinding
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

enum class Evaluation(val evalName: String, val imageId: Int) {
    VeryGood("최고야", R.drawable.ic_frame_category_korea),
    Good("맛있어", R.drawable.ic_frame_category_korea),
    Soso("그냥그래", R.drawable.ic_frame_category_korea),
    Bad("실망이야", R.drawable.ic_frame_category_korea),
    VeryBad("이게뭐야", R.drawable.ic_frame_category_korea)
}