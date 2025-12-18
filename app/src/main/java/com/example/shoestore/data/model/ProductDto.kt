package com.example.shoestore.data.model

import com.google.gson.annotations.SerializedName

data class ProductDto(
    val id: String,
    val title: String,
    @SerializedName("cost") val cost: Double,
    val description: String,
    @SerializedName("category_id") val categoryId: String?,
    @SerializedName("is_best_seller") val isBestSeller: Boolean?
)
