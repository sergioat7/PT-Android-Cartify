package com.aragones.sergio.cartify.presentation.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.aragones.sergio.cartify.domain.model.Product

@Preview
@Composable
fun CartScreenPreview() {
    CartScreen(
        cart = listOf(
            Product("VOUCHER", "Cabify Voucher", 5.0),
            Product("VOUCHER", "Cabify Voucher", 5.0)
        ),
        totalPrice = 5.0,
        onGoBack = {}
    )
}

@Composable
fun CartScreen(cart: List<Product>, totalPrice: Double, onGoBack: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
    }
}
