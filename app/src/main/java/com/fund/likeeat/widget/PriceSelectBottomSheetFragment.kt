package com.fund.likeeat.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fund.likeeat.R
import com.fund.likeeat.adapter.AddReviewBottomSheetListAdapter
import com.fund.likeeat.adapter.GridItem
import com.fund.likeeat.databinding.BottomSheetSelectPriceBinding
import com.fund.likeeat.viewmodels.AddReviewViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PriceSelectBottomSheetFragment: BottomSheetDialogFragment() {

    var addReviewViewModel : AddReviewViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = BottomSheetSelectPriceBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val adapter = AddReviewBottomSheetListAdapter()
        binding.listPrice.adapter = adapter

        val gridItemList: ArrayList<GridItem> = ArrayList()
        Price.values().forEach {
            gridItemList.add(GridItem(it.priceName, it.imageId))
        }
        adapter.submitList(gridItemList)

        binding.btnOk.setOnClickListener {
            addReviewViewModel?.let {
                val selectedItem = adapter.getSelectedItem()
                it.setPrice(selectedItem.name)
            }
            dismiss()
        }

        return binding.root
    }
}

enum class Price(val priceName: String, val imageId: Int) {
    Less10000("만원이하", R.drawable.ic_frame_category_korea),
    More10000("만원~", R.drawable.ic_frame_category_korea),
    More20000("2만원~", R.drawable.ic_frame_category_korea),
    More30000("3만원~", R.drawable.ic_frame_category_korea),
    More50000("5만원~", R.drawable.ic_frame_category_korea),
    More100000("10만원~", R.drawable.ic_frame_category_korea)
}