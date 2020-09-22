package com.fund.likeeat.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fund.likeeat.adapter.ColorItem
import com.fund.likeeat.adapter.ColorListAdapter
import com.fund.likeeat.adapter.ColorSelectedListener
import com.fund.likeeat.databinding.BottomSheetSelectThemeColorBinding
import com.fund.likeeat.utilities.COLOR_NOT_SELECTED
import com.fund.likeeat.utilities.ColorList
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ThemeColorSelectBottomSheetFragment: BottomSheetDialogFragment() {
    var selectedPosition = COLOR_NOT_SELECTED

    lateinit var myColorSavedListener: ColorSavedListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = BottomSheetSelectThemeColorBinding.inflate(inflater, container, false)
        context ?: return binding.root

        var colorSelected = arguments?.getInt("COLOR_SELECTED")

        val adapter = ColorListAdapter(object: ColorSelectedListener {
            override fun onColorSelected(colorCode: Int) {
                colorSelected = colorCode
            }
        })

        binding.listThemeColor.adapter = adapter

        val colorItemList: ArrayList<ColorItem> = ArrayList()
        for((index, item) in ColorList.colorList.withIndex()) {
            colorItemList.add(ColorItem(item.first, item.second))
            if(item.first == colorSelected) {
                selectedPosition = index
            }
        }
        adapter.submitList(colorItemList, selectedPosition)

        binding.actionEnroll.setOnClickListener {
            myColorSavedListener.onSaved(colorSelected!!)
            dismiss()
        }

        return binding.root
    }

    fun setColorSavedListener(li: ColorSavedListener) {
        myColorSavedListener = li
    }

    interface ColorSavedListener {
        fun onSaved(colorCode: Int)
    }
}