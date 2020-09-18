package com.fund.likeeat.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fund.likeeat.adapter.AddReviewBottomSheetListAdapter
import com.fund.likeeat.adapter.GridItem
import com.fund.likeeat.databinding.BottomSheetSelectCompanionBinding
import com.fund.likeeat.manager.Companions
import com.fund.likeeat.viewmodels.AddReviewViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CompanionSelectBottomSheetFragment: BottomSheetDialogFragment() {

    var addReviewViewModel : AddReviewViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = BottomSheetSelectCompanionBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val adapter = AddReviewBottomSheetListAdapter()
        binding.listCompanion.adapter = adapter

        val gridItemList: ArrayList<GridItem> = ArrayList()
        Companions.values().forEach {
            gridItemList.add(GridItem(it.CompanionName, it.imageId))
        }
        adapter.submitList(gridItemList)

        binding.btnOk.setOnClickListener {
            addReviewViewModel?.let {
                val selectedItem = adapter.getSelectedItem()
                it.setCompanion(selectedItem.name)
            }
            dismiss()
        }

        return binding.root
    }
}