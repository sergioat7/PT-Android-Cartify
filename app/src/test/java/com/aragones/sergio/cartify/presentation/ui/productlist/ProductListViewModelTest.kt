package com.aragones.sergio.cartify.presentation.ui.productlist

import com.aragones.sergio.cartify.domain.ProductsRepository
import com.aragones.sergio.cartify.domain.model.Product
import com.aragones.sergio.cartify.utils.MainCoroutineScopeRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductListViewModelTest {

    private val repository: ProductsRepository = mockk()
    private lateinit var sut: ProductListViewModel
    private val voucher = Product("VOUCHER", "Cabify Voucher", 5.0)
    private val tShirt = Product("TSHIRT", "Cabify T-Shirt", 20.0)
    private val mug = Product("MUG", "Cabify Coffee Mug", 7.5)
    private val products = listOf(voucher, tShirt, mug)

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var coroutinesTestRule = MainCoroutineScopeRule()

    @Before
    fun setUp() {
        sut = ProductListViewModel(repository)
    }

    @Test
    fun `WHEN fetch products THEN do expected call`() {

        sut.fetchProducts()

        coVerify(exactly = 1) { repository.getProducts() }
    }

    @Test
    fun `GIVEN products WHEN fetch products THEN load products`() {

        coEvery { repository.getProducts() } returns flow { emit(Result.success(products)) }

        sut.fetchProducts()

        Assert.assertEquals(products, sut.products)
    }

    @Test
    fun `GIVEN product WHEN add product THEN add product to cart`() {

        val product = products.first()

        sut.addProduct(product)

        Assert.assertEquals(product, sut.cart.first())
    }

    @Test
    fun `GIVEN product in cart WHEN remove product THEN remove product from cart`() {

        val product = products.first()
        sut.addProduct(product)
        Assert.assertEquals(1, sut.cart.size)

        sut.removeProduct(product)

        Assert.assertEquals(0, sut.cart.size)
    }
}