package com.fund.likeeat.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fund.likeeat.R
import com.fund.likeeat.adapter.AddReviewBottomSheetListAdapter
import com.fund.likeeat.adapter.GridItem
import com.fund.likeeat.databinding.BottomSheetSelectCompanionBinding
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

enum class Companions(val CompanionName: String, val imageId: Int) {
    Solo("혼자서", R.drawable.ic_frame_category_korea),
    Friends("친구와", R.drawable.ic_frame_category_korea),
    Parents("부모님과", R.drawable.ic_frame_category_korea),
    Lover("여친/남친", R.drawable.ic_frame_category_korea),
    Date("소개팅", R.drawable.ic_frame_category_korea),
    Company("회식에서", R.drawable.ic_frame_category_korea)
}