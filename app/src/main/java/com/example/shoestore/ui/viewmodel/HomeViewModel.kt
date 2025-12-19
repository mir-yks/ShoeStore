package com.example.shoestore.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoestore.data.RetrofitInstance
import com.example.shoestore.data.mapper.ProductImageMapper
import com.example.shoestore.data.mapper.categoryNameFromId
import com.example.shoestore.data.model.Product
import com.example.shoestore.data.model.ProductDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val api = RetrofitInstance.userManagementService

    private val popularProducts = MutableStateFlow<List<Product>>(emptyList())
    val popularProductsFlow = popularProducts.asStateFlow()

    private val isLoading = MutableStateFlow(false)
    val isLoadingFlow = isLoading.asStateFlow()

    private val error = MutableStateFlow<String?>(null)
    val errorFlow = error.asStateFlow()

    fun loadPopularProducts() {
        viewModelScope.launch {
            isLoading.value = true
            error.value = null
            try {
                val response = api.getProducts()
                if (response.isSuccessful) {
                    val allProducts = response.body().orEmpty()
                    val bestSellers = allProducts.filter { it.isBestSeller == true }
                    val result = if (bestSellers.isEmpty()) allProducts else bestSellers
                    popularProducts.value = result.map { it.toUi() }
                } else {
                    error.value = response.code().toString()
                }
            } catch (e: Exception) {
                error.value = e.message
            } finally {
                isLoading.value = false
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
}
