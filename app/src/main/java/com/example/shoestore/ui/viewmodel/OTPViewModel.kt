package com.example.shoestore.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoestore.data.RetrofitInstance
import com.example.shoestore.data.TokenStorage
import com.example.shoestore.data.model.VerifyOtpRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class OTPState {
    object Idle : OTPState()
    object Loading : OTPState()
    object Success : OTPState()
    object Error : OTPState()
}

class OTPViewModel : ViewModel() {
    private val _state = MutableStateFlow<OTPState>(OTPState.Idle)
    val state: StateFlow<OTPState> = _state

    fun verifyCode(email: String, code: String) {
        viewModelScope.launch {
            _state.value = OTPState.Loading
            try {
                val response = RetrofitInstance.userManagementService.verifyOtp(
                    VerifyOtpRequest(email, code, "recovery")
                )
                if (response.isSuccessful) {
                    TokenStorage.accessToken = response.body()?.access_token
                    _state.value = OTPState.Success
                } else {
                    _state.value = OTPState.Error
                }
            } catch (e: Exception) {
                _state.value = OTPState.Error
            }
        }
    }
}