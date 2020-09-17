package com.fund.likeeat.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fund.likeeat.R
import com.fund.likeeat.adapter.AddReviewBottomSheetListAdapter
import com.fund.likeeat.adapter.GridItem
import com.fund.likeeat.databinding.BottomSheetSelectToiletBinding
import com.fund.likeeat.viewmodels.AddReviewViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ToiletSelectBottomSheetFragment: BottomSheetDialogFragment() {

    var addReviewViewModel : AddReviewViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = BottomSheetSelectToiletBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val adapter = AddReviewBottomSheetListAdapter()
        binding.listToilet.adapter = adapter

        val gridItemList: ArrayList<GridItem> = ArrayList()
        Toilet.values().forEach {
            gridItemList.add(GridItem(it.toiletName, it.imageId))
        }
        adapter.submitList(gridItemList)

        binding.btnOk.setOnClickListener {
            addReviewViewModel?.let {
                val selectedItem = adapter.getSelectedItem()
                it.setToilet(selectedItem.name)
            }
            dismiss()
        }

        return binding.root
    }
}

enum class Toilet(val toiletName: String, val imageId: Int) {
    Clean("깨끗해", R.drawable.ic_frame_category_korea),
    Dirty("더러워", R.drawable.ic_frame_category_korea),
    Unisex("남여공용", R.drawable.ic_frame_category_korea),
    Insta("인스타각", R.drawable.ic_frame_category_korea)
}