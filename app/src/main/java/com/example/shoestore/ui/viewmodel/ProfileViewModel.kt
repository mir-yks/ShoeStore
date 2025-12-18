package com.example.shoestore.ui.viewmodels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoestore.data.RetrofitInstance
import com.example.shoestore.data.TokenStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class ProfileViewModel : ViewModel() {

    private val api = RetrofitInstance.userManagementService

    val firstName = MutableStateFlow("")
    val lastName = MutableStateFlow("")
    val address = MutableStateFlow("")
    val phone = MutableStateFlow("")
    val photoBase64 = MutableStateFlow<String?>(null)

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess = _saveSuccess.asStateFlow()

    private var isNewProfile = false

    fun resetSaveSuccess() {
        _saveSuccess.value = false
    }

    fun loadUserProfile(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val token = "Bearer ${TokenStorage.accessToken}"
                val apiKey = TokenStorage.apiKey

                val response = api.getProfile("eq.$userId", token, apiKey)

                if (response.isSuccessful) {
                    val profileList = response.body()
                    if (profileList.isNullOrEmpty()) {
                        isNewProfile = true
                        firstName.value = ""
                        lastName.value = ""
                        address.value = ""
                        phone.value = ""
                        photoBase64.value = null
                    } else {
                        val profile = profileList.first()
                        firstName.value = profile.firstname
                        lastName.value = profile.lastname
                        address.value = profile.address
                        phone.value = profile.phone
                        photoBase64.value = profile.photo
                        isNewProfile = false
                    }
                } else {
                    _errorMessage.value = "Ошибка загрузки профиля: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка сети: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveChanges(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _saveSuccess.value = false
            try {
                val token = "Bearer ${TokenStorage.accessToken}"
                val apiKey = TokenStorage.apiKey

                val dataMap = mutableMapOf(
                    "firstname" to firstName.value,
                    "lastname" to lastName.value,
                    "address" to address.value,
                    "phone" to phone.value,
                    "photo" to photoBase64.value
                )

                val response = if (isNewProfile) {
                    dataMap["user_id"] = userId
                    api.createProfile(dataMap, token, apiKey)
                } else {
                    api.updateProfile("eq.$userId", dataMap, token, apiKey)
                }

                if (response.isSuccessful) {
                    _errorMessage.value = "Профиль успешно сохранен"
                    isNewProfile = false
                    _saveSuccess.value = true
                } else {
                    _errorMessage.value = "Ошибка сохранения: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка сети при сохранении: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setProfilePhoto(bitmap: Bitmap) {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
        val byteArray = outputStream.toByteArray()
        photoBase64.value = Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    fun decodeBase64Photo(base64Str: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            null
        }
    }
}
