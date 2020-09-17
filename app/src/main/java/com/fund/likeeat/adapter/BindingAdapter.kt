package com.fund.likeeat.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
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

@BindingAdapter("isPublic")
fun bindTextIsPublic(view: TextView, isPublic: Boolean) {
    if(isPublic) view.text = "" else view.text = "비공개"
}

@BindingAdapter("imageFromId")
fun bindImageFromId(view: ImageView, imageId: Int) {
    view.setImageResource(imageId)
}
