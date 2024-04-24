package com.aragones.sergio.cartify.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aragones.sergio.cartify.presentation.ui.Routes
import com.aragones.sergio.cartify.presentation.ui.cart.CartScreen
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

                val navigationController = rememberNavController()
                NavHost(
                    navController = navigationController,
                    startDestination = Routes.ProductList.route
                ) {
                    composable(route = Routes.ProductList.route) {
                        ProductListScreen(products = viewModel.products,
                            cart = viewModel.cart,
                            onAddProduct = { viewModel.addProduct(it) },
                            onRemoveProduct = { viewModel.removeProduct(it) },
                            onNavigateToCart = { navigationController.navigate(Routes.Cart.route) })
                    }
                    composable(route = Routes.Cart.route) {
                        CartScreen(cart = viewModel.cart,
                            onGoBack = { navigationController.navigateUp() })
                    }
                }
            }
        }
        viewModel.fetchProducts()
    }
}