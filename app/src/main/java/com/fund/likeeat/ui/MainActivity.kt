package com.fund.likeeat.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil.setContentView
import com.fund.likeeat.R
import com.fund.likeeat.databinding.ActivityMainBinding
import com.fund.likeeat.network.RetrofitProcedure

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView<ActivityMainBinding>(this,
            R.layout.activity_main
        )

        loadData()
    }

    fun loadData() {
        RetrofitProcedure.getPlace()
    }
}