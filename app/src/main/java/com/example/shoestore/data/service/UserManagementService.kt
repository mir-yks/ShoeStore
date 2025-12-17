package com.example.shoestore.data.service

import com.example.shoestore.data.model.SignInRequest
import com.example.shoestore.data.model.SignInResponse
import com.example.shoestore.data.model.SignUpRequest
import com.example.shoestore.data.model.SignUpResponse
import com.example.shoestore.data.model.VerifyOtpRequest
import com.example.shoestore.data.model.VerifyOtpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT

const val API_KEY="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImxqbXBzZ25rYXJjcWJ1d2JjcGpkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjU3NTQ4MDMsImV4cCI6MjA4MTMzMDgwM30.NMKqsx8vzu5lcYtv_lV4kFWgPXYqhevR3Ibj23thZVE"

interface UserManagementService {

    @Headers(
        "apikey: $API_KEY",
        "Content-Type: application/json"
    )
    @POST("auth/v1/signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<SignUpResponse>

    @Headers(
        "apikey: $API_KEY",
        "Content-Type: application/json"
    )
    @POST("auth/v1/token?grant_type=password")
    suspend fun signIn(@Body signInRequest: SignInRequest): Response<SignInResponse>

    @Headers("apikey: $API_KEY", "Content-Type: application/json")
    @POST("auth/v1/verify")
    suspend fun verifyOtp(@Body verifyOtpRequest: VerifyOtpRequest): Response<VerifyOtpResponse>

    @Headers("apikey: $API_KEY", "Content-Type: application/json")
    @POST("auth/v1/recover")
    suspend fun resetPassword(@Body body: Map<String, String>): Response<Unit>

    @Headers("apikey: $API_KEY", "Content-Type: application/json")
    @PUT("auth/v1/user")
    suspend fun updatePassword(
        @Header("Authorization") bearerToken: String,
        @Body body: Map<String, String>
    ): Response<Unit>
}
