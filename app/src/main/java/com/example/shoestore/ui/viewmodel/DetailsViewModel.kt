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

class DetailsViewModel : ViewModel() {
    private val api = RetrofitInstance.userManagementService

    private val _product = MutableStateFlow<Product?>(null)
    val product = _product.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun loadProduct(productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // В API должен быть метод получения по ID. Если его нет, можно получить все и отфильтровать,
                // или добавить метод getProductById(id) в ApiService.
                // Предположим, что мы фильтруем из общего списка или есть метод:
                val response = api.getProductById("eq.$productId") // Используем фильтр Supabase
                if (response.isSuccessful) {
                    val list = response.body()
                    if (!list.isNullOrEmpty()) {
                        _product.value = list[0].toUi()
                    } else {
                        _error.value = "Товар не найден"
                    }
                } else {
                    _error.value = "Ошибка загрузки"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun ProductDto.toUi(): Product =
        Product(
            id = id,
            name = title,
            price = "₽${cost.toInt()}",
            originalPrice = "",
            category = "Men's Shoes",
            imageUrl = "",
            imageResId = ProductImageMapper.productImages[id],
            description = description
        )

}
