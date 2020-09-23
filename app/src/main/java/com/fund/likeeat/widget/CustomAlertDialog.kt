package com.fund.likeeat.widget

import android.content.Context
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.fund.likeeat.R
import kotlinx.android.synthetic.main.dialog_alert_custom.view.*

class CustomAlertDialog(private val context: Context) {

    private val builder: AlertDialog.Builder by lazy {
        AlertDialog.Builder(context).setView(view)
    }

    private val view: View by lazy {
        View.inflate(context, R.layout.dialog_alert_custom, null)
    }

    private var dialog: AlertDialog? = null

    private val onTouchListener = View.OnTouchListener { _, motionEvent ->
        if (motionEvent.action == MotionEvent.ACTION_UP) {
            Handler().postDelayed({
                dismiss()
            }, 5)
        }
        false
    }

    fun setTitle(@StringRes titleId: Int): CustomAlertDialog {
        view.text_title.text = context.getText(titleId)
        return this
    }

    fun setTitle(title: CharSequence): CustomAlertDialog {
        view.text_title.text = title
        return this
    }

    fun setMessage(@StringRes messageId: Int): CustomAlertDialog {
        view.text_content.text = context.getText(messageId)
        return this
    }

    fun setMessage(message: CharSequence): CustomAlertDialog {
        view.text_content.text = message
        return this
    }

    fun setPositiveButton(@StringRes textId: Int, listener: (view: View) -> (Unit)): CustomAlertDialog {
        view.btn_ok.apply {
            text = context.getText(textId)
            setOnClickListener(listener)
            setOnTouchListener(onTouchListener)
        }
        return this
    }

    fun setPositiveButton(text: CharSequence, listener: (view: View) -> (Unit)): CustomAlertDialog {
        view.btn_ok.apply {
            this.text = text
            setOnClickListener(listener)
            setOnTouchListener(onTouchListener)
        }
        return this
    }

    fun setNegativeButton(@StringRes textId: Int, listener: (view: View) -> (Unit)): CustomAlertDialog {
        view.btn_cancel.apply {
            text = context.getText(textId)
            this.text = text
            setOnClickListener(listener)
            setOnTouchListener(onTouchListener)
        }
        return this
    }

    fun setNegativeButton(text: CharSequence, listener: (view: View) -> (Unit)): CustomAlertDialog {
        view.btn_cancel.apply {
            this.text = text
            setOnClickListener(listener)
            setOnTouchListener(onTouchListener)
        }
        return this
    }

    fun create() {
        dialog = builder.create()
    }

    fun show() {
        dialog = builder.create()
        dialog?.show()
    }

    fun dismiss() {
        dialog?.dismiss()
    }

}