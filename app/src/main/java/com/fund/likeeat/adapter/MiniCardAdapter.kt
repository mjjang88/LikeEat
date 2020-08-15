package com.fund.likeeat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fund.likeeat.R
import com.naver.maps.map.overlay.InfoWindow
import kotlinx.android.synthetic.main.item_mini_card.view.*

class MiniCardAdapter(val ctx: Context, val parent: ViewGroup) : InfoWindow.DefaultViewAdapter(ctx) {
    override fun getContentView(p0: InfoWindow): View {
        val view = LayoutInflater.from(ctx).inflate(R.layout.item_mini_card, parent, false)
        view.mini_card_name.text = "히히히히히"
        view.mini_card_address.text = "헤헤헤헤헤"
        return view
    }
}