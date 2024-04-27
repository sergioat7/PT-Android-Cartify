package com.aragones.sergio.cartify.presentation.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aragones.sergio.cartify.domain.model.Discount
import com.aragones.sergio.cartify.domain.model.Product
import com.aragones.sergio.cartify.domain.model.getPriceFor
import com.aragones.sergio.cartify.presentation.ui.productlist.CustomActionButton

@Preview
@Composable
fun CartScreenPreview() {
    CartScreen(
        cart = listOf(
            Product("VOUCHER", "Cabify Voucher", 5.0, discount = Discount.TWO_FOR_ONE),
            Product("VOUCHER", "Cabify Voucher", 5.0, discount = Discount.TWO_FOR_ONE),
            Product("MUG", "Cabify Coffee Mug", 7.5)
        ),
        totalPrice = 5.0,
        onGoBack = {},
        onClearCart = {}
    )
}

@Composable
fun CartScreen(
    cart: List<Product>,
    totalPrice: Double,
    onGoBack: () -> Unit,
    onClearCart: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {

        TopAppBar(
            title = {
                Text(
                    text = "My Cart",
                    style = TextStyle(
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                )
            },
            navigationIcon = {
                IconButton(onClick = { onGoBack() }, modifier = Modifier.testTag("backButton")) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "",
                        tint = MaterialTheme.colors.primary
                    )
                }
            },
            actions = {
                Text(
                    text = "$totalPrice €",
                    modifier = Modifier
                        .padding(end = 24.dp)
                        .testTag("totalPrice"),
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

        if (cart.isNotEmpty()) {
            Button(
                onClick = { onClearCart() },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 24.dp, end = 24.dp)
                    .testTag("clearCartButton"),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.secondary
                )
            ) {
                Text(text = "Clear all")
            }
        }

        LazyColumn(
            modifier = Modifier
                .padding(24.dp)
                .weight(1f)
        ) {
            items(items = cart.groupBy { it.discount }.toList()) { values ->

                val discount = values.first
                val products = values.second

                val productsName = buildString {
                    products.groupBy { it.code }.map {
                        val text = "${it.value.first().name} (${it.value.size}), "
                        append(text)
                    }
                }.dropLast(2)
                val realPrice = products.sumOf { it.price }
                val price = discount.getPriceFor(products)
                val discountIsApplied = discount != null && realPrice != price

                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 12.dp)
                        .testTag("productCard"),
                    backgroundColor = MaterialTheme.colors.secondary,
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
                            Text(
                                text = productsName,
                                modifier = Modifier.testTag("productName"),
                                style = TextStyle(
                                    color = MaterialTheme.colors.onSecondary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            )
                            Row {
                                if (discountIsApplied) {
                                    Text(
                                        text = "$realPrice €",
                                        modifier = Modifier
                                            .padding(top = 8.dp, end = 8.dp)
                                            .testTag("productRealPrice"),
                                        style = TextStyle(
                                            color = MaterialTheme.colors.onSecondary,
                                            fontSize = 16.sp,
                                            textDecoration = TextDecoration.LineThrough
                                        )
                                    )
                                }
                                Text(
                                    text = "$price €",
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                        .testTag("productPrice"),
                                    style = TextStyle(
                                        color = MaterialTheme.colors.onSecondary,
                                        fontSize = 16.sp
                                    )
                                )
                            }
                            if (discountIsApplied) {
                                Text(
                                    text = "Discount applied: ${discount?.description}",
                                    modifier = Modifier.padding(top = 8.dp).testTag("discount"),
                                    style = TextStyle(
                                        color = MaterialTheme.colors.onSecondary,
                                        fontSize = 16.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        CustomActionButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(100.dp)
                .padding(24.dp)
                .align(Alignment.CenterHorizontally)
                .testTag("confirmOrderButton"),
            enabled = cart.isNotEmpty(),
            shape = RoundedCornerShape(24.dp),
            text = "Confirm order"
        )
    }
}
