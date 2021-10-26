package com.example.viewpaggerdemo.util

import android.content.Context
import android.net.ConnectivityManager
import com.example.viewpaggerdemo.core.ImageLoadingApplication

object CommonUtils {
    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}