package com.fund.likeeat.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fund.likeeat.R
import com.fund.likeeat.adapter.AddReviewBottomSheetListAdapter
import com.fund.likeeat.adapter.GridItem
import com.fund.likeeat.databinding.BottomSheetSelectRevisitBinding
import com.fund.likeeat.databinding.BottomSheetSelectToiletBinding
import com.fund.likeeat.viewmodels.AddReviewViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RevisitSelectBottomSheetFragment: BottomSheetDialogFragment() {

    var addReviewViewModel : AddReviewViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = BottomSheetSelectRevisitBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val adapter = AddReviewBottomSheetListAdapter()
        binding.listRevisit.adapter = adapter

        val gridItemList: ArrayList<GridItem> = ArrayList()
        Revisit.values().forEach {
            gridItemList.add(GridItem(it.revisitName, it.imageId))
        }
        adapter.submitList(gridItemList)

        binding.btnOk.setOnClickListener {
            addReviewViewModel?.let {
                val selectedItem = adapter.getSelectedItem()
                it.setRevisit(selectedItem.name)
            }
            dismiss()
        }

        return binding.root
    }
}

enum class Revisit(val revisitName: String, val imageId: Int) {
    Always("재방문각", R.drawable.ic_frame_category_korea),
    Sometimes("근처오면", R.drawable.ic_frame_category_korea),
    Never("다신안와", R.drawable.ic_frame_category_korea)
}