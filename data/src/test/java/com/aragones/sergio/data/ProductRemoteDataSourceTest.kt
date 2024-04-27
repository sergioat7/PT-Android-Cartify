package com.aragones.sergio.data

import com.aragones.sergio.data.model.Product
import com.aragones.sergio.data.model.Products
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ProductRemoteDataSourceTest {

    private val api: ProductApi = mockk()
    private lateinit var sut: ProductRemoteDataSource
    private val products = Products(
        listOf(
            Product("VOUCHER", "Cabify Voucher", 5.0),
            Product("TSHIRT", "Cabify T-Shirt", 20.0),
            Product("MUG", "Cabify Coffee Mug", 7.5)
        )
    )
    private val exception = RuntimeException("Network error")

    @Before
    fun setUp() {
        sut = ProductRemoteDataSource(api)
    }

    @Test
    fun `WHEN fetch products THEN do expected call`() = runTest {

        sut.fetchProducts().first()

        coVerify(exactly = 1) { api.fetchProducts() }
    }

    @Test
    fun `GIVEN successful response WHEN fetch products THEN return products`() = runTest {

        coEvery { api.fetchProducts() } returns products

        val result = sut.fetchProducts().first()

        Assert.assertEquals(Result.success(products), result)
    }

    @Test
    fun `GIVEN failure response WHEN fetch products THEN return exception`() = runTest {

        coEvery { api.fetchProducts() } throws exception

        val result = sut.fetchProducts().first()

        Assert.assertEquals("There was a problem fetching data", result.exceptionOrNull()?.message)
        Assert.assertEquals(exception, result.exceptionOrNull()?.cause)
    }
}