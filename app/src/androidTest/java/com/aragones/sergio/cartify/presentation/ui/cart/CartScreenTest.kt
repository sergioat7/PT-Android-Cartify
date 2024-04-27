package com.aragones.sergio.cartify.presentation.ui.cart

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.aragones.sergio.domain.model.Discount
import com.aragones.sergio.domain.model.Product
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class CartScreenTest {

    private val voucher = Product("VOUCHER", "Cabify Voucher", 5.0, Discount.TWO_FOR_ONE)
    private val mug = Product("MUG", "Cabify Coffee Mug", 7.5)
    private val cart = listOf(voucher, voucher, mug).sortedBy { it.code }

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun given_cart_when_showCart_then_showProductsGroupedByCodeAndShowTotalPriceAndShowClearButtonAndEnableConfirmOrderButton() {

        composeTestRule.setContent {
            CartScreen(
                cart = cart,
                totalPrice = 12.5,
                onGoBack = {},
                onClearCart = {}
            )
        }

        composeTestRule.onNodeWithTag("totalPrice").assertTextEquals("12.5 €")
        composeTestRule.onNodeWithTag("clearCartButton").assertIsDisplayed()

        val products = cart.groupBy { it.code }
        composeTestRule.onAllNodesWithTag("productCard").assertCountEquals(products.size)
        val productNameNodes = composeTestRule.onAllNodesWithTag("productName")
        val productPriceNodes = composeTestRule.onAllNodesWithTag("productPrice")
        for ((index, entry) in products.entries.withIndex()) {

            val product = entry.value.first()
            productNameNodes[index].assertTextEquals("${product.name} (${entry.value.size})")
            productPriceNodes[index].assertTextEquals("${product.price} €")
        }

        composeTestRule.onNodeWithTag("confirmOrderButton").assertIsEnabled()
    }

    @Test
    fun given_cartWithDiscountsApplied_when_showCart_then_showProductRealPriceStrikeTroughAndShowPriceAndShowDiscountApplied() {

        composeTestRule.setContent {
            CartScreen(
                cart = cart,
                totalPrice = 12.5,
                onGoBack = {},
                onClearCart = {}
            )
        }

        composeTestRule.onAllNodesWithTag("productRealPrice").apply {
            assertCountEquals(1)
            onFirst().assertTextEquals("10.0 €")
        }
        composeTestRule.onAllNodesWithTag("productPrice").apply {
            assertCountEquals(2)
            onFirst().assertTextEquals("${mug.price} €")
            onLast().assertTextEquals("${voucher.price} €")
        }
        composeTestRule.onAllNodesWithTag("discount").apply {
            assertCountEquals(1)
            onFirst().assertTextEquals("Discount applied: ${voucher.discount?.description}")
        }
    }

    @Test
    fun given_emptyCart_when_showCart_then_hideProductsAndHideClearCartButtonAndDisableConfirmOrderButton() {

        composeTestRule.setContent {
            CartScreen(
                cart = listOf(),
                totalPrice = 0.0,
                onGoBack = {},
                onClearCart = {}
            )
        }

        composeTestRule.onNodeWithTag("totalPrice").assertTextEquals("0.0 €")
        composeTestRule.onNodeWithTag("clearCartButton").assertIsNotDisplayed()
        composeTestRule.onAllNodesWithTag("productCard").assertCountEquals(0)
        composeTestRule.onAllNodesWithTag("productName").assertCountEquals(0)
        composeTestRule.onAllNodesWithTag("productPrice").assertCountEquals(0)
        composeTestRule.onNodeWithTag("confirmOrderButton").assertIsNotEnabled()
    }

    @Test
    fun given_cart_when_clearCart_then_removeProductsFromCart() {

        val otherCart = mutableListOf<Product>()
        otherCart.addAll(cart)

        composeTestRule.setContent {
            CartScreen(
                cart = otherCart,
                totalPrice = 12.5,
                onGoBack = {},
                onClearCart = {
                    otherCart.clear()
                }
            )
        }

        Assert.assertEquals(3, otherCart.size)
        composeTestRule.onNodeWithTag("clearCartButton").performClick()
        Assert.assertEquals(0, otherCart.size)
    }

    @Test
    fun when_clickBackButton_navigateBackToProductList() {

        var goBack = false

        composeTestRule.setContent {
            CartScreen(
                cart = cart,
                totalPrice = 12.5,
                onGoBack = {
                    goBack = true
                },
                onClearCart = {}
            )
        }

        composeTestRule.onNodeWithTag("backButton").performClick()
        Assert.assertEquals(true, goBack)
    }
}