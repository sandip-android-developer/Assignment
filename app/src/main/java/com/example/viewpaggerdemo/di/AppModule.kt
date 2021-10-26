package com.example.viewpaggerdemo.di

import android.content.Context
import androidx.room.Room
import com.example.viewpaggerdemo.api.ApiService
import com.example.viewpaggerdemo.core.ImageLoadingApplication
import com.example.viewpaggerdemo.other.AppConstant
import com.example.viewpaggerdemo.other.ChangeBaseUrlInterceptor
import com.example.viewpaggerdemo.repository.local.AppDao
import com.example.viewpaggerdemo.repository.local.AppDatabase
import com.example.viewpaggerdemo.repository.remote.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun getChangeURLInterceptor(): ChangeBaseUrlInterceptor {
        return ChangeBaseUrlInterceptor.get()
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: ChangeBaseUrlInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(AppConstant.BASE_URL_BOOK)
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun providesRepository(apiService: ApiService, dao: AppDao) = MainRepository(apiService, dao)


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(appContext, AppDatabase::class.java, "assignment.db").build()

    @Singleton
    @Provides
    fun provideLogDao(database: AppDatabase) = database.appDao()

}