package com.fund.likeeat.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fund.likeeat.adapter.AddReviewBottomSheetListAdapter
import com.fund.likeeat.adapter.GridItem
import com.fund.likeeat.databinding.BottomSheetSelectCategoryBinding
import com.fund.likeeat.manager.Category
import com.fund.likeeat.viewmodels.AddReviewViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_select_category.*

class CategorySelectBottomSheetFragment: BottomSheetDialogFragment() {

    var addReviewViewModel : AddReviewViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = BottomSheetSelectCategoryBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val adapter = AddReviewBottomSheetListAdapter()
        binding.listCategory.adapter = adapter

        val gridItemList: ArrayList<GridItem> = ArrayList()
        Category.values().forEach {
            gridItemList.add(GridItem(it.categoryName, it.imageId))
        }
        adapter.submitList(gridItemList)

        addReviewViewModel?.editedReview?.value?.category?.let {
            adapter.setSelectItem(it)
        }

        binding.btnOk.setOnClickListener {
            addReviewViewModel?.let {
                val selectedItem = adapter.getSelectedItem()
                it.setCategory(selectedItem.name)
            }
            dismiss()
        }

        return binding.root
    }
}