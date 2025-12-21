package com.ssti.dharmendrapractical.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carts")
data class CartsResponse(
    @PrimaryKey
    val id: Int = 1, // Default ID for Room storage since API doesn't provide one
    val carts: List<Cart>,
    val limit: Int,
    val skip: Int,
    val total: Int
) {
    data class Cart(
        val discountedTotal: Double,
        val id: Int,
        val products: List<Product>,
        val total: Double,
        val totalProducts: Int,
        val totalQuantity: Int,
        val userId: Int
    ) {
        data class Product(
            val discountPercentage: Double,
            val discountedTotal: Double,
            val id: Int,
            val price: Double,
            val quantity: Int,
            val thumbnail: String,
            val title: String,
            val total: Double
        )
    }
}