package com.kevinrsebastian.androidboilerplate.di

import android.content.Context
import android.os.Environment
import com.kevinrsebastian.androidboilerplate.BuildConfig
import com.kevinrsebastian.androidboilerplate.api.MockApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class ApiModule {

    @Provides
    @Singleton
    fun provideMockApi(retrofit: Retrofit): MockApi {
        return retrofit.create(MockApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://61e80425e32cd90017acbf6f.mockapi.io/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        // Cache size
        val isWritable = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
        val cacheDir = if (isWritable) context.externalCacheDir else context.cacheDir
        val diskSize = 250 * 1024 * 1024
        val cache = Cache(File(cacheDir, "temps"), diskSize.toLong())

        return OkHttpClient.Builder().apply {
            cache(cache)
            // Add header interceptor
            val headerInterceptor = Interceptor { chain ->
                val originalRequest = chain.request()
                val request = originalRequest.newBuilder().build()
                chain.proceed(request)
            }
            addInterceptor(headerInterceptor)
            // Debug interceptors
            if (BuildConfig.DEBUG) {
                // Logging
                addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            }
        }.build()
    }
}
