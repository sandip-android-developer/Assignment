package com.example.viewpaggerdemo.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "table_list_image")
data class ListViewImage(
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @SerializedName("imageUrl")
    var imageUrl: String? = ""
)