package com.example.shoestore.data.network

import com.example.shoestore.data.model.UserProfile
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {
    @GET("rest/v1/profiles?select=*")
    suspend fun getUserProfile(
        @Header("apikey") apiKey: String,
        @Header("Authorization") token: String
    ): List<UserProfile>

    // Добавили query-параметр id для фильтрации, чтобы не было ошибки 400
    @PATCH("rest/v1/profiles")
    suspend fun updateProfile(
        @Header("apikey") apiKey: String,
        @Header("Authorization") token: String,
        @Query("user_id") userIdFilter: String, // Фильтр: eq.твой_uuid
        @Body profile: UserProfile
    ): Response<Unit>
}

object RetrofitClient {
    private const val BASE_URL = "https://ljmpsgnkarcqbuwbcpjd.supabase.co/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}