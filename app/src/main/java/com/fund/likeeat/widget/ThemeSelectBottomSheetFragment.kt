package com.fund.likeeat.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.fund.likeeat.adapter.AddReviewThemeAdapter
import com.fund.likeeat.data.Theme
import com.fund.likeeat.databinding.BottomSheetSelectThemeBinding
import com.fund.likeeat.viewmodels.AddReviewViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ThemeSelectBottomSheetFragment: BottomSheetDialogFragment() {

    var addReviewViewModel : AddReviewViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = BottomSheetSelectThemeBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val adapter = AddReviewThemeAdapter()
        binding.listTheme.adapter = adapter

        lifecycleScope.launch(Dispatchers.IO) {
            val themeList = addReviewViewModel?.getThemeList()
            val themeIds = addReviewViewModel?.editedReview?.value?.themeIds?.split(",")
            val checkedList: ArrayList<Theme> = ArrayList()

            themeIds?.forEach { themeId ->
                if (themeId.isNullOrBlank()) return@forEach
                themeList?.find {
                    it.id == themeId.toLong()
                }.apply {
                    if (this != null) {
                        checkedList.add(this)
                    }
                }
            }
            adapter.checkedList = checkedList

            adapter.submitList(themeList)
        }

        binding.btnOk.setOnClickListener {
            var strThemeList = String()
            adapter.checkedList.sortBy { theme ->
                theme.id
            }
            adapter.checkedList.forEach {
                if (strThemeList.isNotBlank()) {
                    strThemeList += ","
                }
                strThemeList += it.id
            }

            addReviewViewModel?.setTheme(strThemeList)

            dismiss()
        }

        return binding.root
    }
}