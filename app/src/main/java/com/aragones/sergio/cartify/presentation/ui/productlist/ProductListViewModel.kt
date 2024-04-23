package com.aragones.sergio.cartify.presentation.ui.productlist

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aragones.sergio.cartify.domain.ProductsRepository
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
}