package com.fund.likeeat.ui

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import com.fund.likeeat.R
import com.fund.likeeat.network.RetrofitProcedure
import kotlinx.android.synthetic.main.dialog_delete_theme.*

class DeleteThemeDialog(val activity: Activity, val id: Long) : Dialog(activity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        window?.attributes = layoutParams

        setContentView(R.layout.dialog_delete_theme)
        delete_theme_cancel.setOnClickListener { dismiss() }
        delete_theme_ok.setOnClickListener {
            RetrofitProcedure.deleteThemeById(activity, id)
            dismiss()
        }
    }
}