package com.aragones.sergio.data.model

data class Products(
    val products: List<Product>
)

data class Product(
    val code: String,
    val name: String,
    val price: Double
)