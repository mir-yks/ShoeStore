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

class HomeViewModel : ViewModel() {

    private val api = RetrofitInstance.userManagementService

    private val _popularProducts = MutableStateFlow<List<Product>>(emptyList())
    val popularProducts = _popularProducts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun loadPopularProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = api.getProducts()
                if (response.isSuccessful) {
                    val allProducts = response.body().orEmpty()
                    val bestSellers = allProducts.filter { it.isBestSeller == true }
                    _popularProducts.value = bestSellers.map { it.toUi() }
                } else {
                    _error.value = "Ошибка загрузки: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Ошибка сети: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun ProductDto.toUi(): Product = Product(
        id = id,
        name = title,
        price = cost.toInt().toString(),
        originalPrice = "",
        category = categoryNameFromId(categoryId),
        imageUrl = "",
        imageResId = ProductImageMapper.productImages[id] ?: 0,
        description = description
    )

    fun categoryNameFromId(id: String?): String = when (id) {
        "ea4ed603-8cbe-4d57-a359-b6b843a645bc" -> "Outdoor"
        "4f3a690b-41bf-4fca-8ffc-67cc385c6637" -> "Tennis"
        "76ab9d74-7d5b-4dee-9c67-6ed4019fa202" -> "Men"
        "8143b506-d70a-41ec-a5eb-3cf09627da9e" -> "Women"
        else -> "All"
    }
}
