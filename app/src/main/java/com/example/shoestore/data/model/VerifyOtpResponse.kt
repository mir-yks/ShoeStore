package com.example.shoestore.data.model

data class VerifyOtpResponse(
    val access_token: String,
    val user: UserData
)

data class UserData(
    val id: String,
    val email: String
)