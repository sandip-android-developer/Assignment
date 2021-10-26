package com.example.viewpaggerdemo.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "table_book")
class Book(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    var id: Int= 0,
    @SerializedName("title")
    var title: String? = "",

    @SerializedName("description")
    var description: String? = "",

    @SerializedName("contributor")
    var contributor: String? = "",

    @SerializedName("author")
    var author: String? = "",

    @SerializedName("price")
    var price: Int? = 0,
    @SerializedName("publisher")
    var publisher: String? = ""
)