package com.example.shoestore.data.model

import com.google.gson.annotations.SerializedName

data class UserProfile(
    val id: String? = null,

    @SerializedName("user_id")
    val userId: String,

    val firstname: String = "",
    val lastname: String = "",
    val address: String = "",
    val phone: String = "",

    val photo: String? = null
)
