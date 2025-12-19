package com.example.shoestore.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoestore.R
import com.example.shoestore.data.model.Category
import com.example.shoestore.data.model.Product
import com.example.shoestore.ui.components.ProductCard
import com.example.shoestore.ui.theme.AppTypography
import com.example.shoestore.ui.theme.ShoeShopTheme
import com.example.shoestore.ui.viewmodel.CatalogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    onProductClick: (Product) -> Unit,
    viewModel: CatalogViewModel = viewModel()
) {
    val context = LocalContext.current
    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var selectedCategory by remember { mutableStateOf("All") }

    val categories = listOf(
        Category("All"),
        Category("Outdoor"),
        Category("Tennis"),
        Category("Men"),
        Category("Women")
    )

    val filteredProducts = if (selectedCategory == "All") {
        products
    } else {
        products.filter { it.category == selectedCategory }
    }

    LaunchedEffect(Unit) {
        viewModel.loadCatalog()
    }

    LaunchedEffect(error) {
        error?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
    }

    ShoeShopTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.catalog_title),
                            style = AppTypography.headingRegular32
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { }) {
                            Icon(
                                painter = painterResource(id = R.drawable.back),
                                contentDescription = null
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(Color(0xFFF7F7F9))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.categories),
                            style = AppTypography.bodyMedium16,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(horizontal = 0.dp)
                        ) {
                            items(categories) { category ->
                                CategoryChip(
                                    category = category.name,
                                    isSelected = selectedCategory == category.name,
                                    onClick = { selectedCategory = category.name }
                                )
                            }
                        }
                    }

                    if (isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else if (filteredProducts.isEmpty() && error == null) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.catalog_empty),
                                style = AppTypography.bodyMedium16
                            )
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(filteredProducts) { product ->
                                ProductCard(
                                    product = product,
                                    onProductClick = { onProductClick(product) },
                                    onFavoriteClick = {}
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryChip(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White,
        contentColor = if (isSelected) Color.White else Color.Black
    ) {
        Text(
            text = category,
            style = AppTypography.bodyMedium16.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
