package com.aragones.sergio.cartify.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aragones.sergio.cartify.domain.model.Product

@Preview
@Composable
fun ProductListScreenPreview() {
    ProductListScreen(
        products = listOf(
            Product("VOUCHER", "Cabify Voucher", 5.0),
            Product("TSHIRT", "Cabify T-Shirt", 20.0),
            Product("MUG", "Cabify Coffee Mug", 7.5)
        ),
        cart = listOf(
            Product("VOUCHER", "Cabify Voucher", 5.0),
            Product("VOUCHER", "Cabify Voucher", 5.0)
        ),
        onAddProduct = {},
        onRemoveProduct = {}
    )
}

@Composable
fun ProductListScreen(
    products: List<Product>,
    cart: List<Product>,
    onAddProduct: (Product) -> Unit,
    onRemoveProduct: (Product) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {

        TopAppBar(
            title = {
                Text(
                    text = "Products",
                    style = TextStyle(
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                )
            },
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 8.dp
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            items(products) { product ->

                val count = cart.filter { it.code == product.code }.size

                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 12.dp),
                    backgroundColor = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(modifier = Modifier.padding(12.dp)) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = "",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp)
                        ) {
                            Text(text = product.name)
                            Text(text = "${product.price} €")
                            Text(text = "Quantity: $count")
                        }
                        Button(
                            onClick = { onAddProduct(product) },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.secondary
                            ),
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Text(text = "Add")
                        }
                    }
                }
            }
        }
    }
}