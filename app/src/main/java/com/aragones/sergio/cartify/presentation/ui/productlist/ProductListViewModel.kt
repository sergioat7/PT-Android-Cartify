package com.aragones.sergio.cartify.presentation.ui.productlist

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aragones.sergio.cartify.domain.ProductsRepository
import com.aragones.sergio.cartify.domain.model.Discount
import com.aragones.sergio.cartify.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: ProductsRepository
) : ViewModel() {

    private val _products = mutableStateListOf<Product>()
    val products: List<Product> = _products

    private val _cart = mutableStateListOf<Product>()
    val cart: List<Product> = _cart

    fun fetchProducts() = viewModelScope.launch {
        repository.getProducts().collect { result ->

            if (result.isSuccess) {
                _products.addAll(result.getOrDefault(listOf()))
            } else {
                print("There were an error")//TODO: show error to user
            }
        }
    }

    fun addProduct(product: Product) {
        _cart.add(product)
    }

    fun removeProduct(product: Product) {
        _cart.remove(product)
    }

    fun getPriceWithDiscounts(): Double {

        return cart.groupBy { it.code }.mapValues { values ->
            val products = values.value

            values.value.first().discount?.let { discount ->
                when (discount) {
                    Discount.TWO_FOR_ONE -> {
                        products.chunked(2)
                            .map { it.first() }
                            .sumOf { it.price }
                    }

                    Discount.MORE_THAN_3 -> {
                        if (products.size > 2) {
                            products.sumOf { 19.0 }
                        } else {
                            products.sumOf { it.price }
                        }
                    }
                }
            } ?: run {
                products.sumOf { it.price }
            }
        }.values.sum()
    }
}