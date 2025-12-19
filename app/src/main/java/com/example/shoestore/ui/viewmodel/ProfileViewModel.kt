package com.example.shoestore.ui.viewmodels

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoestore.data.model.UserProfile
import com.example.shoestore.data.network.RetrofitClient
import kotlinx.coroutines.launch

sealed class ProfileUiState {
    object Idle : ProfileUiState()
    object Loading : ProfileUiState()
    object Success : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

class ProfileViewModel : ViewModel() {
    private val API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImxqbXBzZ25rYXJjcWJ1d2JjcGpkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjU3NTQ4MDMsImV4cCI6MjA4MTMzMDgwM30.NMKqsx8vzu5lcYtv_lV4kFWgPXYqhevR3Ibj23thZVE"
    private val AUTH_TOKEN = "Bearer $API_KEY"
    // В реальном приложении ID берется из Auth-сессии. Пока для теста:
    private val USER_ID_FILTER = "eq.89af550a-ddc0-4314-9f78-b6aef9f65778"

    var uiState by mutableStateOf<ProfileUiState>(ProfileUiState.Idle)
        private set

    var isEditing by mutableStateOf(false)

    // Данные профиля
    var name by mutableStateOf("")
    var lastName by mutableStateOf("")
    var address by mutableStateOf("")
    var phone by mutableStateOf("")
    var bitmapPhoto by mutableStateOf<Bitmap?>(null)

    fun loadProfile() {
        viewModelScope.launch {
            uiState = ProfileUiState.Loading
            try {
                val profileList = RetrofitClient.apiService.getUserProfile(API_KEY, AUTH_TOKEN)
                if (profileList.isNotEmpty()) {
                    val profile = profileList[0]
                    name = profile.firstName ?: ""
                    lastName = profile.lastName ?: ""
                    address = profile.address ?: ""
                    phone = profile.phone ?: ""
                }
                uiState = ProfileUiState.Success
            } catch (e: Exception) {
                uiState = ProfileUiState.Error("Загрузка не удалась")
            }
        }
    }

    fun saveProfile() {
        viewModelScope.launch {
            uiState = ProfileUiState.Loading
            try {
                val profile = UserProfile(name, lastName, address, phone)
                // Передаем USER_ID_FILTER, чтобы Supabase знал, КТО обновляется
                val response = RetrofitClient.apiService.updateProfile(
                    API_KEY, AUTH_TOKEN, USER_ID_FILTER, profile
                )
                if (response.isSuccessful) {
                    isEditing = false
                    uiState = ProfileUiState.Success
                } else {
                    uiState = ProfileUiState.Error("Ошибка 400: Проверь фильтр ID или структуру JSON")
                }
            } catch (e: Exception) {
                uiState = ProfileUiState.Error("Сеть недоступна")
            }
        }
    }

    fun onPhotoCaptured(bitmap: Bitmap) {
        bitmapPhoto = bitmap
        // Тут можно добавить логику отправки фото в Storage Supabase
    }

    fun dismissError() { uiState = ProfileUiState.Success }
}