package com.aragones.sergio.cartify.presentation.ui.productlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aragones.sergio.cartify.R
import com.aragones.sergio.cartify.domain.model.Discount
import com.aragones.sergio.cartify.domain.model.Product

@Preview
@Composable
fun ProductListScreenPreview() {
    ProductListScreen(
        products = listOf(
            Product("VOUCHER", "Cabify Voucher", 5.0, discount = Discount.TWO_FOR_ONE),
            Product("TSHIRT", "Cabify T-Shirt", 20.0, discount = Discount.MORE_THAN_3),
            Product("MUG", "Cabify Coffee Mug", 7.5)
        ),
        cart = listOf(
            Product("VOUCHER", "Cabify Voucher", 5.0),
            Product("VOUCHER", "Cabify Voucher", 5.0)
        ),
        totalPrice = 5.0,
        onAddProduct = {},
        onRemoveProduct = {},
        onNavigateToCart = {}
    )
}

@Composable
fun ProductListScreen(
    products: List<Product>,
    cart: List<Product>,
    totalPrice: Double,
    onAddProduct: (Product) -> Unit,
    onRemoveProduct: (Product) -> Unit,
    onNavigateToCart: () -> Unit
) {

    val buttonWidth = 100.dp
    val buttonHeight = 40.dp
    val buttonCornerRadius = 12.dp

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
                .padding(24.dp)
                .weight(1f)
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
                            Text(
                                text = product.name,
                                style = TextStyle(
                                    color = MaterialTheme.colors.onPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            )
                            Text(
                                text = "${product.price} €",
                                modifier = Modifier.padding(top = 8.dp),
                                style = TextStyle(
                                    color = MaterialTheme.colors.onPrimary,
                                    fontSize = 16.sp
                                )
                            )
                            product.discount?.let { discount ->
                                Text(
                                    text = "Discount: ${discount.description}",
                                    modifier = Modifier.padding(top = 8.dp),
                                    style = TextStyle(
                                        color = MaterialTheme.colors.onPrimary,
                                        fontSize = 16.sp
                                    )
                                )
                            }
                        }
                        if (count == 0) {
                            CustomActionButton(
                                onClick = { onAddProduct(product) },
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .width(buttonWidth)
                                    .height(buttonHeight),
                                shape = RoundedCornerShape(buttonCornerRadius),
                                enabled = true,
                                text = "Add"
                            )
                        } else {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .widthIn(buttonWidth, Dp.Unspecified)
                                    .height(buttonHeight)
                            ) {
                                CustomIconButton(
                                    icon = painterResource(id = R.drawable.baseline_remove_24),
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .width(buttonHeight),
                                    shape = RoundedCornerShape(
                                        topStart = buttonCornerRadius,
                                        bottomStart = buttonCornerRadius
                                    )
                                ) {
                                    onRemoveProduct(product)
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .background(MaterialTheme.colors.surface)
                                ) {
                                    Text(
                                        text = "$count",
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .padding(horizontal = 4.dp),
                                        style = TextStyle(
                                            color = MaterialTheme.colors.primary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                                CustomIconButton(
                                    icon = painterResource(id = R.drawable.baseline_add_24),
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .width(buttonHeight),
                                    shape = RoundedCornerShape(
                                        topEnd = buttonCornerRadius,
                                        bottomEnd = buttonCornerRadius
                                    )
                                ) {
                                    onAddProduct(product)
                                }
                            }
                        }
                    }
                }
            }
        }

        if (cart.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primaryVariant)
            ) {
                Text(
                    text = "$totalPrice €",
                    modifier = Modifier.padding(24.dp),
                    style = TextStyle(
                        color = MaterialTheme.colors.onPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { onNavigateToCart() },
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primaryVariant
                    )
                ) {
                    Text(
                        text = "View cart (${cart.count()})",
                        modifier = Modifier.padding(24.dp),
                        style = TextStyle(
                            color = MaterialTheme.colors.onPrimary,
                            fontSize = 24.sp
                        )
                    )
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = "",
                        modifier = Modifier.align(Alignment.CenterVertically),
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun CustomIconButton(
    icon: Painter,
    modifier: Modifier,
    shape: Shape,
    onClick: () -> Unit
) {

    Button(
        onClick = { onClick() },
        modifier = modifier,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.secondary
        ),
        contentPadding = PaddingValues(12.dp)
    ) {
        Icon(
            painter = icon, contentDescription = "", tint = MaterialTheme.colors.onSecondary
        )
    }
}

@Composable
fun CustomActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean,
    shape: Shape,
    text: String
) {

    Button(
        onClick = { onClick() },
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.secondary
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.button,
            color = MaterialTheme.colors.onSecondary
        )
    }
}