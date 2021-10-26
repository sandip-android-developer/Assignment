package com.example.viewpaggerdemo.model.responsepojo

import com.google.gson.annotations.SerializedName

open class ImageResponse {
    @SerializedName("breeds")
    var breeds: List<Any>? = listOf()

    @SerializedName("categories")
    var categories: List<Any>? = listOf()

    @SerializedName("id")
    var id: String? = ""

    @SerializedName("url")
    var url: String? = ""
}