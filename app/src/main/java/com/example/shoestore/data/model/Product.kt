package com.example.shoestore.data.model

data class Product(
    val id: String,
    val name: String,
    val price: String,
    val originalPrice: String,
    val category: String,
    val imageUrl: String = "",
    val imageResId: Int? = null
)