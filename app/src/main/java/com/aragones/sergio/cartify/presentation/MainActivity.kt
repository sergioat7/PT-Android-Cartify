package com.aragones.sergio.cartify.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.aragones.sergio.cartify.presentation.ui.productlist.ProductListScreen
import com.aragones.sergio.cartify.presentation.ui.productlist.ProductListViewModel
import com.aragones.sergio.cartify.presentation.ui.theme.CartifyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: ProductListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CartifyTheme {
                ProductListScreen(
                    products = viewModel.products,
                    cart = viewModel.cart,
                    onAddProduct = { viewModel.addProduct(it) },
                    onRemoveProduct = { viewModel.removeProduct(it) })
            }
        }
        viewModel.fetchProducts()
    }
}