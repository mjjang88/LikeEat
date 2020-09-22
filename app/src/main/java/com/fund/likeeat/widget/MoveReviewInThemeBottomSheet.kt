package com.fund.likeeat.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.fund.likeeat.R
import com.fund.likeeat.adapter.MoveThemeAdapter
import com.fund.likeeat.adapter.OnClickCardListener
import com.fund.likeeat.databinding.BottomSheetSelectNewThemeBinding
import com.fund.likeeat.databinding.BottomSheetSetReviewInThemeBinding
import com.fund.likeeat.manager.MyApplication
import com.fund.likeeat.utilities.ToastUtil
import com.fund.likeeat.viewmodels.AllThemesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MoveReviewInThemeBottomSheet : BottomSheetDialogFragment() {
    var reviewId: Long? = null
    var themeId: Long? = null
    private val viewModel: AllThemesViewModel by viewModel { parametersOf(MyApplication.pref.uid) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        reviewId = arguments?.getLong("REVIEW_ID")
        themeId = arguments?.getLong("THEME_ID")

        val binding = DataBindingUtil.inflate<BottomSheetSelectNewThemeBinding>(
            inflater,
            R.layout.bottom_sheet_select_new_theme,
            container,
            false
        ).apply {
            lifecycleOwner = viewLifecycleOwner

            actionEnroll.setOnClickListener {
                // TODO 수정코드 작성
                ToastUtil.toastShort("${reviewId}번 리뷰가 ${themeId}번 테마로 이동됨 (시뮬레이션)")
                dismiss()
            }
        }

        val adapter = MoveThemeAdapter().apply {
            setOnClickCardListener(object: OnClickCardListener {
                override fun onClick(themeId: Long) {
                    this@MoveReviewInThemeBottomSheet.themeId = themeId
                }
            })
        }
        binding.listTheme.adapter = adapter

        viewModel.themeList.observe(requireActivity()) { themeList ->
            for((index, theme) in themeList.withIndex()) {
                if(theme.id == themeId) {
                    adapter.selectedPosition = index
                    break
                }
            }
            adapter.submitList(themeList)
        }

        return binding.root
    }
}