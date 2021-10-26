package com.example.viewpaggerdemo.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "table_grid_image")
data class GridViewImage(
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @SerializedName("imageUrl")
    var imageUrl: String? = ""
)