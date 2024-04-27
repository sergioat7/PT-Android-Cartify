package com.aragones.sergio.domain.model

data class Product(
    val code: String,
    val name: String,
    val price: Double,
    var discount: Discount? = null
)