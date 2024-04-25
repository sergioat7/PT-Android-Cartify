package com.aragones.sergio.cartify.domain

import com.aragones.sergio.cartify.data.remote.ProductRemoteDataSource
import com.aragones.sergio.cartify.data.remote.model.Product
import com.aragones.sergio.cartify.data.remote.model.Products
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ProductsRepositoryTest {

    private val dataSource: ProductRemoteDataSource = mockk()
    private lateinit var sut: ProductsRepository
    private val remoteProducts = Products(
        listOf(
            Product("VOUCHER", "Cabify Voucher", 5.0),
            Product("TSHIRT", "Cabify T-Shirt", 20.0),
            Product("MUG", "Cabify Coffee Mug", 7.5)
        )
    )
    private val domainProducts = remoteProducts.products.map { it.toDomain() }
    private val exception = RuntimeException("Network error")

    @Before
    fun setUp() {
        sut = ProductsRepository(dataSource)
    }

    @Test
    fun `WHEN get products THEN do expected call`() = runTest {

        coEvery { dataSource.fetchProducts() } returns flow { emit(Result.success(remoteProducts)) }

        sut.getProducts()

        coVerify(exactly = 1) { dataSource.fetchProducts() }
    }

    @Test
    fun `GIVEN remote products WHEN get products THEN return mapped products`() = runTest {

        coEvery { dataSource.fetchProducts() } returns flow { emit(Result.success(remoteProducts)) }

        val result = sut.getProducts().first()

        Assert.assertEquals(domainProducts, result.getOrNull())
    }

    @Test
    fun `GIVEN error WHEN get product THEN propagate error`() = runTest {

        coEvery { dataSource.fetchProducts() } returns flow { emit(Result.failure(exception)) }

        val result = sut.getProducts().first()

        Assert.assertEquals(exception, result.exceptionOrNull())
    }
}