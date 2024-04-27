package com.aragones.sergio.data.di

import com.aragones.sergio.data.ProductApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun getMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    @Singleton
    fun getRetrofit(moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl("https://gist.githubusercontent.com/")
        .client(OkHttpClient())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @Singleton
    fun getProductApi(retrofit: Retrofit): ProductApi = retrofit.create(ProductApi::class.java)
}