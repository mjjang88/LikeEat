package com.fund.likeeat.widget

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.fund.likeeat.R
import com.fund.likeeat.network.RetrofitProcedure
import com.fund.likeeat.utilities.ToastUtil
import kotlinx.android.synthetic.main.dialog_delete_review_in_theme.*
import kotlinx.android.synthetic.main.dialog_delete_theme.*

class DeleteReviewInThemeDialog(context: Context, val reviewId: Long) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        window?.attributes = layoutParams

        setContentView(R.layout.dialog_delete_review_in_theme)
        delete_cancel.setOnClickListener { dismiss() }
        delete_ok.setOnClickListener {
            // TODO 삭제코드 작성
            ToastUtil.toastShort("ReviewId: $reviewId 삭제 시뮬레이션 (DeleteReviewInThemeDialog)")
            // RetrofitProcedure.deleteReviewById(reviewId)
            dismiss()
        }
    }
}