package com.aragones.sergio.cartify.data.remote

import com.aragones.sergio.cartify.data.remote.model.Products
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRemoteDataSource @Inject constructor(private val api: ProductApi) {

    suspend fun fetchProducts(): Flow<Result<Products>> {
        return flow {
            emit(Result.success(api.fetchProducts()))
        }.catch {
            emit(Result.failure(RuntimeException("There was a problem fetching data", it)))
        }
    }
}