package com.fund.likeeat.ui

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.fund.likeeat.R
import com.fund.likeeat.databinding.BottomSheetSetThemeBinding
import com.fund.likeeat.viewmodels.OneThemeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_set_theme.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SetThemeBottomSheet: BottomSheetDialogFragment() {
    var themeId: Long? = null
    var isChangedPublicState = false
    private val themeViewModel: OneThemeViewModel by viewModel { parametersOf(themeId) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        themeId = arguments?.getLong("THEME_ID")

        val binding = DataBindingUtil.inflate<BottomSheetSetThemeBinding>(inflater, R.layout.bottom_sheet_set_theme, container, false)
            .apply {
                viewModel = themeViewModel
                lifecycleOwner = viewLifecycleOwner

                switchPublic.setOnCheckedChangeListener { _, isChecked ->
                    if(isChecked) themePublic.text = ""
                    else themePublic.text = "비공개"

                    isChangedPublicState = true
                }

                btnEditName.setOnClickListener {
                    val intent = Intent(activity, ChangeThemeNameActivity::class.java).apply {
                        putExtra("THEME_ID", themeId)
                        putExtra("THEME_NAME", theme_name.text.toString())
                    }
                    startActivity(intent)
                    dismiss()
                }

                btnDeleteTheme.setOnClickListener {
                    val dialog = DeleteThemeDialog(requireContext(), themeId ?: throw Exception())
                    dialog.setCancelable(false)
                    dialog.show()
                    dismiss()
                }

            }
        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        if(isChangedPublicState) {
            themeViewModel.updateTheme(themeId!!, isPublic = switch_public.isChecked)
        }

        super.onDismiss(dialog)
    }
}