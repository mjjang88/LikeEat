package com.fund.likeeat.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fund.likeeat.R
import com.fund.likeeat.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_place_add.*

class AddPlaceActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_place_add
        )

        btn_add_review.setOnClickListener {
            addReview()
        }
    }

    private fun addReview() {
        if (isInputEmpty()) {
            Toast.makeText(this, "입력하지 않은 데이터가 존재합니다", Toast.LENGTH_LONG).show()
            return
        }

        // TODO: 2020-08-03 리뷰 추가 기능 구현 필요

        Toast.makeText(this, "맛집 추가 완료 했습니다.", Toast.LENGTH_LONG).show()
        finish()
    }

    private fun isInputEmpty() : Boolean {
        if (input_place_name.editText?.text.toString().isNullOrBlank() ||
            input_place_one_line_review.editText?.text.toString().isNullOrBlank() ||
            input_place_x.editText?.text.toString().isNullOrBlank() ||
            input_place_y.editText?.text.toString().isNullOrBlank()) {
            return true
        }
        return false
    }
}