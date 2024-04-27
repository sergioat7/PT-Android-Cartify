package com.aragones.sergio.cartify.presentation.ui.productlist

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aragones.sergio.domain.ProductsRepository
import com.aragones.sergio.domain.model.Product
import com.aragones.sergio.domain.model.getPriceFor
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
                _products.addAll(result.getOrDefault(listOf()).sortedBy { it.code })
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

    fun clearCart() {
        _cart.clear()
    }

    fun getPriceWithDiscounts(): Double {

        return cart.groupBy { it.discount }.mapValues { values ->

            val discount = values.key
            val products = values.value
            discount.getPriceFor(products)
        }.values.sum()
    }
}