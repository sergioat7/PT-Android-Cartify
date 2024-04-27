package com.aragones.sergio.domain

import com.aragones.sergio.domain.model.Discount
import com.aragones.sergio.domain.model.Product
import com.aragones.sergio.data.model.Product as RemoteProduct

fun RemoteProduct.toDomain(): Product {

    return Product(code, name, price).also {
        when (code) {
            "VOUCHER" -> it.discount = Discount.TWO_FOR_ONE
            "TSHIRT" -> it.discount = Discount.MORE_THAN_3
        }
    }
}