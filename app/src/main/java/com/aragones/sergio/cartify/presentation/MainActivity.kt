package com.aragones.sergio.cartify.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
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

                val price = viewModel.getPriceWithDiscounts()

                val navigationController = rememberNavController()
                NavHost(
                    navController = navigationController,
                    startDestination = Routes.ProductList.route
                ) {
                    composable(
                        route = Routes.ProductList.route,
                        exitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(700)
                            )
                        },
                        popEnterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(700)
                            )
                        }
                    ) {
                        ProductListScreen(products = viewModel.products,
                            cart = viewModel.cart,
                            totalPrice = price,
                            onAddProduct = { viewModel.addProduct(it) },
                            onRemoveProduct = { viewModel.removeProduct(it) },
                            onNavigateToCart = { navigationController.navigate(Routes.Cart.route) })
                    }
                    composable(
                        route = Routes.Cart.route,
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(700)
                            )
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(700)
                            )
                        }
                    ) {
                        CartScreen(
                            cart = viewModel.cart.sortedBy { it.code },
                            totalPrice = price,
                            onGoBack = { navigationController.navigateUp() },
                            onClearCart = { viewModel.clearCart() }
                        )
                    }
                }
            }
        }
        viewModel.fetchProducts()
    }
}