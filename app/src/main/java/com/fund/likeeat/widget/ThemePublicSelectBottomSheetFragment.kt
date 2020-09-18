package com.fund.likeeat.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fund.likeeat.R
import com.fund.likeeat.adapter.*
import com.fund.likeeat.databinding.BottomSheetSelectThemeColorBinding
import com.fund.likeeat.databinding.BottomSheetSelectThemePublicBinding
import com.fund.likeeat.utilities.ColorList
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ThemePublicSelectBottomSheetFragment: BottomSheetDialogFragment() {
    lateinit var myPublicSavedListener: PublicSavedListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = BottomSheetSelectThemePublicBinding.inflate(inflater, container, false)
        context ?: return binding.root

        var isPublicSelected = arguments?.getBoolean("PUBLIC_SELECTED")
        val selectedPosition = if(isPublicSelected!!) 0 else 1

        val adapter = ThemePublicListAdapter(object: PublicSelectedListener {
            override fun onPublicSelected(isPublic: Boolean) {
                isPublicSelected = isPublic
            }
        })

        binding.listThemePublic.adapter = adapter

        val publicItemList: ArrayList<GridPublicItem> = arrayListOf(
            GridPublicItem("공개", R.drawable.ic_baseline_visibility_24, true),
            GridPublicItem("비공개", R.drawable.ic_baseline_visibility_off_24, false)
        )

        adapter.submitList(publicItemList, selectedPosition)

        binding.actionEnroll.setOnClickListener {
            myPublicSavedListener.onSaved(isPublicSelected!!)
            dismiss()
        }

        return binding.root
    }

    fun setPublicSavedListener(li: PublicSavedListener) {
        myPublicSavedListener = li
    }

    interface PublicSavedListener {
        fun onSaved(isPublic: Boolean)
    }
}