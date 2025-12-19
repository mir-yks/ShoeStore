package com.example.shoestore.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoestore.data.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ForgotPasswordState {
    object Idle : ForgotPasswordState()
    object Loading : ForgotPasswordState()
    object Success : ForgotPasswordState()
    data class Error(val message: String) : ForgotPasswordState()
}

class ForgotPasswordViewModel : ViewModel() {
    private val _state = MutableStateFlow<ForgotPasswordState>(ForgotPasswordState.Idle)
    val state: StateFlow<ForgotPasswordState> = _state

    fun sendResetCode(email: String) {
        viewModelScope.launch {
            _state.value = ForgotPasswordState.Loading
            try {
                val response = RetrofitInstance.userManagementService.resetPassword(mapOf("email" to email))

                if (response.isSuccessful) {
                    Log.d("ForgotPassword", "Письмо отправлено на $email")
                    _state.value = ForgotPasswordState.Success
                } else {
                    val errorMsg = when(response.code()) {
                        429 -> "Слишком много запросов. Подождите минуту."
                        else -> "Ошибка: ${response.code()}"
                    }
                    _state.value = ForgotPasswordState.Error(errorMsg)
                }
            } catch (e: Exception) {
                _state.value = ForgotPasswordState.Error("Проверьте интернет соединение")
                Log.e("ForgotPassword", e.message.toString())
            }
        }
    }

    fun resetState() {
        _state.value = ForgotPasswordState.Idle
    }
}