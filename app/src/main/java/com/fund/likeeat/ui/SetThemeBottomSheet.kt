package com.fund.likeeat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.fund.likeeat.R
import com.fund.likeeat.databinding.BottomSheetSetThemeBinding
import com.fund.likeeat.viewmodels.SetThemeBottomSheetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SetThemeBottomSheet: BottomSheetDialogFragment() {
    private val setThemeBottomSheetViewModel: SetThemeBottomSheetViewModel by viewModel { parametersOf(arguments?.getLong("THEME_ID")) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<BottomSheetSetThemeBinding>(inflater, R.layout.bottom_sheet_set_theme, container, false)
            .apply {
                viewModel = setThemeBottomSheetViewModel
                lifecycleOwner = viewLifecycleOwner

                switchPublic.setOnCheckedChangeListener { _, isChecked ->
                    if(isChecked) themePublic.text = ""
                    else themePublic.text = "비공개"
                }
            }
        return binding.root
    }
}