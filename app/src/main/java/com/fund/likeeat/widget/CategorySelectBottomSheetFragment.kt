package com.fund.likeeat.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fund.likeeat.R
import com.fund.likeeat.adapter.AddReviewBottomSheetListAdapter
import com.fund.likeeat.adapter.GridItem
import com.fund.likeeat.databinding.BottomSheetSelectCategoryBinding
import com.fund.likeeat.viewmodels.AddReviewViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

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

enum class Category(val categoryName: String, val imageId: Int) {
    KoreanFood("한식", R.drawable.ic_frame_category_korea),
    ChineseFood("중식", R.drawable.ic_frame_category_china),
    JapaneseFood("일식", R.drawable.ic_frame_category_japan),
    WesternFood("양식", R.drawable.ic_frame_category_restaurant),
    AsianFood("아시안", R.drawable.ic_frame_category_asian),
    WorldFood("세계", R.drawable.ic_frame_category_world),
    SnackBar("분식", R.drawable.ic_frame_category_snack),
    Cafe("카페", R.drawable.ic_frame_category_cafe),
    FastFood("패스트푸드", R.drawable.ic_frame_category_fastfood);
}