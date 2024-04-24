package com.aragones.sergio.cartify.presentation.ui

sealed class Routes(val route: String) {
    data object ProductList: Routes("product_list")
    data object Cart: Routes("cart")
}