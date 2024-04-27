package com.aragones.sergio.cartify.presentation.ui.productlist

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.aragones.sergio.cartify.domain.model.Discount
import com.aragones.sergio.cartify.domain.model.Product
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class ProductListScreenTest {

    private val voucher = Product("VOUCHER", "Cabify Voucher", 5.0, Discount.TWO_FOR_ONE)
    private val tShirt = Product("TSHIRT", "Cabify T-Shirt", 20.0, Discount.MORE_THAN_3)
    private val mug = Product("MUG", "Cabify Coffee Mug", 7.5)
    private val products = listOf(voucher, tShirt, mug).sortedBy { it.code }

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun given_products_when_showProductList_then_showProducts() {

        composeTestRule.setContent {
            ProductListScreen(
                products = products,
                cart = listOf(),
                totalPrice = 0.0,
                onAddProduct = {},
                onRemoveProduct = {},
                onNavigateToCart = {}
            )
        }

        composeTestRule.onAllNodesWithTag("productCard").assertCountEquals(products.size)

        val productNameNodes = composeTestRule.onAllNodesWithTag("productName")
        val productPriceNodes = composeTestRule.onAllNodesWithTag("productPrice")
        for (index in products.indices) {

            val product = products[index]
            productNameNodes[index].assertTextEquals(product.name)
            productPriceNodes[index].assertTextEquals("${product.price} €")
        }
    }

    @Test
    fun given_productsWithAndWithoutDiscount_when_showProductList_then_showDiscountWhenNeeds() {

        composeTestRule.setContent {
            ProductListScreen(
                products = products,
                cart = listOf(),
                totalPrice = 0.0,
                onAddProduct = {},
                onRemoveProduct = {},
                onNavigateToCart = {}
            )
        }

        val productsWithDiscount = products.filter { it.discount != null }
        val productDiscountNodes = composeTestRule.onAllNodesWithTag("productDiscount")
            .assertCountEquals(productsWithDiscount.size)
        for (index in productsWithDiscount.indices) {

            val product = productsWithDiscount[index]
            productDiscountNodes[index].assertTextEquals("Discount: ${product.discount?.description}")
        }
    }

    @Test
    fun given_emptyCart_when_showProductList_then_showAddButtonAndHideCartSummary() {

        composeTestRule.setContent {
            ProductListScreen(
                products = listOf(voucher),
                cart = listOf(),
                totalPrice = 0.0,
                onAddProduct = {},
                onRemoveProduct = {},
                onNavigateToCart = {}
            )
        }

        composeTestRule.onNodeWithTag("addProductButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("modifyProductCountButtons").assertIsNotDisplayed()
        composeTestRule.onNodeWithTag("cartSummary").assertIsNotDisplayed()
    }

    @Test
    fun given_fullCart_when_showProductList_then_showModifyProductCountButtonsAndShowProductCountAndShowCartSummary() {

        val cart = listOf(mug, mug, mug, tShirt, tShirt)

        composeTestRule.setContent {
            ProductListScreen(
                products = listOf(mug, tShirt),
                cart = cart,
                totalPrice = 62.5,
                onAddProduct = {},
                onRemoveProduct = {},
                onNavigateToCart = {}
            )
        }

        composeTestRule.onNodeWithTag("addProductButton").assertIsNotDisplayed()
        composeTestRule.onAllNodesWithTag("modifyProductCountButtons").apply {
            onFirst().assertIsDisplayed()
            onLast().assertIsDisplayed()
        }
        composeTestRule.onAllNodesWithTag("productCount").apply {
            onFirst().assertTextEquals("3")
            onLast().assertTextEquals("2")
        }
        composeTestRule.onNodeWithTag("cartSummary").assertIsDisplayed()
        composeTestRule.onNodeWithTag("totalPrice").assertTextEquals("62.5 €")
        composeTestRule.onNodeWithTag("cartCount", useUnmergedTree = true)
            .assertTextEquals("View cart (${cart.size})")
    }

    @Test
    fun given_emptyCart_when_addProductToCart_then_productIsAddedToCart() {

        val cart = mutableListOf<Product>()

        composeTestRule.setContent {
            ProductListScreen(
                products = listOf(voucher),
                cart = cart,
                totalPrice = 0.0,
                onAddProduct = {
                    cart.add(it)
                },
                onRemoveProduct = {},
                onNavigateToCart = {}
            )
        }

        Assert.assertEquals(0, cart.size)
        composeTestRule.onNodeWithTag("addProductButton").performClick()
        Assert.assertEquals(1, cart.size)
    }

    @Test
    fun given_fullCart_when_addProductToCart_then_productIsAddedToCart() {

        val cart = mutableListOf(voucher)

        composeTestRule.setContent {
            ProductListScreen(
                products = listOf(voucher),
                cart = cart,
                totalPrice = 0.0,
                onAddProduct = {
                    cart.add(it)
                },
                onRemoveProduct = {},
                onNavigateToCart = {}
            )
        }

        Assert.assertEquals(1, cart.size)
        composeTestRule.onNodeWithTag("plusButton").performClick()
        Assert.assertEquals(2, cart.size)
    }

    @Test
    fun when_removeProductFromCart_then_productIsRemovedFromCart() {

        val cart = mutableListOf(mug, mug, mug)

        composeTestRule.setContent {
            ProductListScreen(
                products = listOf(mug),
                cart = cart,
                totalPrice = 0.0,
                onAddProduct = {},
                onRemoveProduct = {
                    cart.remove(it)
                },
                onNavigateToCart = {}
            )
        }

        Assert.assertEquals(3, cart.size)
        composeTestRule.onNodeWithTag("minusButton").performClick()
        Assert.assertEquals(2, cart.size)
    }

    @Test
    fun given_cartSummaryBeingShown_when_clickViewCart_then_navigateToCart() {

        var goToCart = false

        composeTestRule.setContent {
            ProductListScreen(
                products = products,
                cart = listOf(voucher, mug, tShirt),
                totalPrice = 0.0,
                onAddProduct = {},
                onRemoveProduct = {},
                onNavigateToCart = {
                    goToCart = true
                }
            )
        }

        composeTestRule.onNodeWithTag("viewCartButton").performClick()
        Assert.assertEquals(true, goToCart)
    }

    @Test
    fun given_cartSummaryBeingShown_when_clickViewCartSeveralTimes_then_navigateToCartJustOnce() {

        var goToCart = 0

        composeTestRule.setContent {
            ProductListScreen(
                products = products,
                cart = listOf(voucher, mug, tShirt),
                totalPrice = 0.0,
                onAddProduct = {},
                onRemoveProduct = {},
                onNavigateToCart = {
                    goToCart += 1
                }
            )
        }

        composeTestRule.onNodeWithTag("viewCartButton").performClick()
        composeTestRule.onNodeWithTag("viewCartButton").performClick()
        composeTestRule.onNodeWithTag("viewCartButton").performClick()
        composeTestRule.onNodeWithTag("viewCartButton").performClick()
        Assert.assertEquals(1, goToCart)
    }
}