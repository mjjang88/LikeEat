package com.fund.likeeat.utilities

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fund.likeeat.R
import com.fund.likeeat.manager.MyApplication
import kotlinx.android.synthetic.main.dialog_toast.view.*

object ToastUtil {
    fun toastShort(message: String) {
        val inflater = MyApplication.applicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val toastView = inflater.inflate(
            R.layout.dialog_toast,
            View(MyApplication.applicationContext()).findViewById(R.id.viewgroup) // 쓸데없는 View 객체가 생성.. 상관없나..?
        )?.apply { toast_message.text = message }

        Toast(MyApplication.applicationContext()).apply {
            setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0)
            duration = Toast.LENGTH_SHORT
            setMargin(0F, 0.15F)
            view = toastView
            show()
        }
    }

    fun toastShort(activity: AppCompatActivity, message: String) {
        val inflater = MyApplication.applicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val toastView = inflater.inflate(
            R.layout.dialog_toast,
            activity.findViewById(R.id.viewgroup)
        )?.apply { toast_message.text = message }

        Toast(MyApplication.applicationContext()).apply {
            setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0)
            duration = Toast.LENGTH_SHORT
            setMargin(0F, 0.15F)
            view = toastView
            show()
        }
    }
}