package com.example.viewpaggerdemo.view.base

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

class BindingAdapter {
    companion object {
        @BindingAdapter("app:imageUrl")
        @JvmStatic
        fun loadImage(view: ImageView, url: String) {
            Glide.with(view.context).load(url ?: "").into(view)
        }
    }
}