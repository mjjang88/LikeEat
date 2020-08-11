package com.fund.likeeat.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

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