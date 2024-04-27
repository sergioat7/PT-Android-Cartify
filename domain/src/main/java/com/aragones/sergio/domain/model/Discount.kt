package com.aragones.sergio.domain.model

enum class Discount(val description: String) {
    TWO_FOR_ONE("Buy 2, Pay 1"),
    MORE_THAN_3("Buy 3+ for 19€ each")
}

fun Discount?.getPriceFor(products: List<Product>): Double {

    return when (this) {
        Discount.TWO_FOR_ONE -> {
            products.filterIndexed { index, _ -> index % 2 == 0 }
                .sumOf { it.price }
        }

        Discount.MORE_THAN_3 -> {
            if (products.size > 2) {
                products.sumOf { 19.0 }
            } else {
                products.sumOf { it.price }
            }
        }

        null -> products.sumOf { it.price }
    }
}
