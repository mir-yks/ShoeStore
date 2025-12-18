package com.example.shoestore.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoestore.data.RetrofitInstance
import com.example.shoestore.data.mapper.ProductImageMapper
import com.example.shoestore.data.model.Product
import com.example.shoestore.data.model.ProductDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CatalogViewModel : ViewModel() {

    private val api = RetrofitInstance.userManagementService

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun loadCatalog() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = api.getProducts()
                if (response.isSuccessful) {
                    val list = response.body().orEmpty()
                    _products.value = list.map { it.toUi() }
                } else {
                    _error.value = "Ошибка загрузки каталога: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Ошибка сети: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun ProductDto.toUi(): Product =
        Product(
            id = id,
            name = title,
            price = "${cost.toInt()} ₽",
            originalPrice = "",
            category = "",
            imageUrl = "",
            imageResId = ProductImageMapper.productImages[id]
        )
}
