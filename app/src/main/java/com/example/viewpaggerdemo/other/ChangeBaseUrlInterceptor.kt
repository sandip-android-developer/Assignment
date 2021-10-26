package com.example.viewpaggerdemo.other

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Singleton

@Singleton
class ChangeBaseUrlInterceptor private constructor() : Interceptor {
    private var mScheme: String? = null
    private var mHost: String? = null
    fun setInterceptor(url: String?) {
        val httpUrl = url!!.toHttpUrlOrNull()
        mScheme = httpUrl!!.scheme
        mHost = httpUrl.host
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var original: Request = chain.request()

        // If new Base URL is properly formatted than replace with old one
        if (mScheme != null && mHost != null) {
            val newUrl = original.url.newBuilder()
                .scheme(mScheme!!)
                .host(mHost!!)
                .build()
            original = original.newBuilder()
                .url(newUrl)
                .build()
        }
        return chain.proceed(original)
    }

    companion object {
        private var sInterceptor: ChangeBaseUrlInterceptor? = null
        fun get(): ChangeBaseUrlInterceptor {
            if (sInterceptor == null) {
                sInterceptor = ChangeBaseUrlInterceptor()
            }
            return sInterceptor as ChangeBaseUrlInterceptor
        }
    }
}