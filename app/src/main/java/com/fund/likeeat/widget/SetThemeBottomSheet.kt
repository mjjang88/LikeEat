package com.fund.likeeat.widget

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.fund.likeeat.R
import com.fund.likeeat.databinding.BottomSheetSetThemeBinding
import com.fund.likeeat.ui.UpdateThemeActivity
import com.fund.likeeat.viewmodels.OneThemeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SetThemeBottomSheet: BottomSheetDialogFragment() {
    var themeId: Long? = null
    private val themeViewModel: OneThemeViewModel by viewModel { parametersOf(themeId) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        themeId = arguments?.getLong("THEME_ID")

        val binding = DataBindingUtil.inflate<BottomSheetSetThemeBinding>(inflater, R.layout.bottom_sheet_set_theme, container, false)
            .apply {
                viewModel = themeViewModel
                lifecycleOwner = viewLifecycleOwner

                actionEditTheme.setOnClickListener {
                    val intent = Intent(activity, UpdateThemeActivity::class.java).apply {
                        putExtra("THEME_ID", themeId)
                    }
                    startActivity(intent)
                    dismiss()
                }

                actionDeleteTheme.setOnClickListener {
                    val dialog = DeleteThemeDialog(
                        requireContext(),
                        themeId ?: throw Exception()
                    )
                    dialog.setCancelable(false)
                    dialog.show()
                    dismiss()
                }

            }
        return binding.root
    }
}