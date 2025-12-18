package com.example.shoestore.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoestore.data.RetrofitInstance
import com.example.shoestore.data.TokenStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UpdatePasswordState {
    object Idle : UpdatePasswordState()
    object Loading : UpdatePasswordState()
    object Success : UpdatePasswordState()
    data class Error(val message: String) : UpdatePasswordState()
}

class UpdatePasswordViewModel : ViewModel() {
    private val _state = MutableStateFlow<UpdatePasswordState>(UpdatePasswordState.Idle)
    val state: StateFlow<UpdatePasswordState> = _state

    fun updatePassword(newPassword: String) {
        val token = TokenStorage.accessToken
        if (token == null) {
            _state.value = UpdatePasswordState.Error("Сессия истекла")
            return
        }

        viewModelScope.launch {
            _state.value = UpdatePasswordState.Loading
            try {
                val response = RetrofitInstance.userManagementService.updatePassword(
                    "Bearer $token",
                    mapOf("password" to newPassword)
                )
                if (response.isSuccessful) {
                    _state.value = UpdatePasswordState.Success
                } else {
                    _state.value = UpdatePasswordState.Error("Ошибка: ${response.code()}")
                }
            } catch (e: Exception) {
                _state.value = UpdatePasswordState.Error(e.message ?: "Ошибка сети")
            }
        }
    }

    fun resetState() {
        _state.value = UpdatePasswordState.Idle
    }
}