package com.example.viewpaggerdemo.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.viewpaggerdemo.model.local.*

@Database(entities = [GridViewImage::class, ImagePager::class, ListViewImage::class,Book::class],version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao():AppDao
}