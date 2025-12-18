package com.example.shoestore.data.model

import com.google.gson.annotations.SerializedName

data class FavouriteDto(
    val id: String,
    @SerializedName("product_id") val productId: String?,
    @SerializedName("user_id") val userId: String?
)
