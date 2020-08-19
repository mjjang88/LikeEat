package com.fund.likeeat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fund.likeeat.R
import kotlinx.android.synthetic.main.item_theme_mini.view.*

// 임시로 Array<String>을 만들어뒀음.. 나중에는 리뷰에 해당하는 테마를 가져오는 식으로 처리?
class ReviewsThemeAdapter(val list: Array<Pair<Int, String>>): RecyclerView.Adapter<ReviewsThemeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsThemeViewHolder {
        return ReviewsThemeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_theme_mini, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ReviewsThemeViewHolder, position: Int) {
        val item = list[position]
        holder.circle.circleColor = item.first
        holder.name.text = item.second
    }

}

class ReviewsThemeViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val circle = view.theme_circle
    val name = view.theme_name
}