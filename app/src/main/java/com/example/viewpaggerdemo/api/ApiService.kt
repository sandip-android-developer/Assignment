package com.example.viewpaggerdemo.api

import com.example.viewpaggerdemo.other.AppConstant
import com.example.viewpaggerdemo.BuildConfig
import com.example.viewpaggerdemo.model.responsepojo.BookListResponse
import com.example.viewpaggerdemo.model.responsepojo.ImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    @GET(AppConstant.Search)
    suspend fun getCatImage(
        @Header("x-api-key") API_KEY: String = BuildConfig.API_KEY,
        @Query("limit") limit:Int,
        @Query("page") page:Int
    ): Response<List<ImageResponse>>

    @GET(AppConstant.book)
    suspend fun bookList(
        @Query("api-key") API_KEY_BOOK: String = BuildConfig.API_KEY_BOOK,
        @Query("list") list:String="hardcover-fiction"
    ): Response<BookListResponse>
}