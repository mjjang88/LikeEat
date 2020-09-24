package com.fund.likeeat.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.fund.likeeat.R
import com.fund.likeeat.manager.MyApplication
import com.mikhaellopez.circleview.CircleView

@BindingAdapter("imageFromUri")
fun bindImageFromUri(view: ImageView, imageUri: String?) {
    if (!imageUri.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(imageUri)
            .transition(DrawableTransitionOptions.withCrossFade())
            .transform(CenterCrop(), RoundedCorners(16))
            .into(view)
    }
}

@BindingAdapter("colorCode")
fun bindIconColor(view: CircleView, colorCode: Int) {
    view.circleColor = colorCode
}

@BindingAdapter("colorTagTint")
fun bindTagColor(view: ImageView, colorTagTint: Int) {
    view.setColorFilter(colorTagTint)
}

@BindingAdapter("isPublic")
fun bindTextIsPublic(view: TextView, isPublic: Boolean) {
    if(isPublic) view.text = "" else view.text = "비공개"
}

@BindingAdapter("imageFromId")
fun bindImageFromId(view: ImageView, imageId: Int) {
    view.setImageResource(imageId)
}

@BindingAdapter("reviewsCount")
fun bindReviewCountToString(view: TextView, reviewsCount: Int) {
    view.text = MyApplication.applicationContext().resources.getString(R.string.review_count).replace("num", reviewsCount.toString())
}

@BindingAdapter("setDate")
fun bindSetDate(view: TextView, date: String?) {
    date?.let {
        if (it.length == 8) {
            val year = it.substring(0, 4)
            val month = it.substring(4, 6)
            val day = it.substring(6, 8)

            view.text = "$year.$month.$day"
        }
    }
}