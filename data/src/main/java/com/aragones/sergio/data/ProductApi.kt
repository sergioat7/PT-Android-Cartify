package com.aragones.sergio.data

import com.aragones.sergio.data.model.Products
import retrofit2.http.GET

interface ProductApi {

    @GET("palcalde/6c19259bd32dd6aafa327fa557859c2f/raw/ba51779474a150ee4367cda4f4ffacdcca479887/Products.json")
    suspend fun fetchProducts(): Products
}