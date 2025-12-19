package com.example.shoestore.data.model

import com.google.gson.annotations.SerializedName

data class UserProfile(
    @SerializedName("firstname") val firstName: String,
    @SerializedName("lastname") val lastName: String,
    @SerializedName("address") val address: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("photo") val photoUrl: String? = null
)