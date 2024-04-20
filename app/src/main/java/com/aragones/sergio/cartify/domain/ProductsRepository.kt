package com.aragones.sergio.cartify.domain

import com.aragones.sergio.cartify.data.remote.ProductRemoteDataSource
import com.aragones.sergio.cartify.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductsRepository @Inject constructor(private val remoteDataSource: ProductRemoteDataSource) {

    suspend fun getProducts(): Flow<Result<List<Product>>> =
        remoteDataSource.fetchProducts().map { result ->
            if (result.isSuccess) {

                val products = result.getOrNull()?.let {
                    it.products.map { product -> product.toDomain() }
                } ?: listOf()
                Result.success(products)
            } else {
                Result.failure(
                    result.exceptionOrNull()
                        ?: RuntimeException("There was a problem parsing data source exception")
                )
            }
        }
}