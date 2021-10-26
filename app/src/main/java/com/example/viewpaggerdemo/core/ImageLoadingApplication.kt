package com.example.viewpaggerdemo.core

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ImageLoadingApplication : Application() {
    init {
        Instance = this
    }

    companion object {
        private var Instance: ImageLoadingApplication? = null
        fun applicationContext(): Context {
            return Instance!!.applicationContext
        }
    }
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}