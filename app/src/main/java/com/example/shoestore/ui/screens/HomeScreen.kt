package com.example.shoestore.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoestore.R
import com.example.shoestore.data.model.Category
import com.example.shoestore.data.model.Product
import com.example.shoestore.ui.components.ProductCard
import com.example.shoestore.ui.theme.AppTypography
import com.example.shoestore.ui.theme.ShoeShopTheme
import com.example.shoestore.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onProductClick: (Product) -> Unit,
    onCartClick: () -> Unit,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit = {},
    onProfileEditClick: () -> Unit = {},
    onProfileLogoutClick: () -> Unit = {},
    onOpenCatalog: () -> Unit = {},
    viewModel: HomeViewModel = viewModel()
) {
    var selected by rememberSaveable { mutableIntStateOf(0) }
    var selectedCategory by remember { mutableStateOf("Все") }

    val popularProducts by viewModel.popularProducts.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPopularProducts()
    }

    val categories = listOf(
        Category(name = stringResource(R.string.category_all)),
        Category(name = stringResource(R.string.category_outdoor)),
        Category(name = stringResource(R.string.category_tennis)),
        Category(name = stringResource(R.string.category_men)),
        Category(name = stringResource(R.string.category_women))
    )

    ShoeShopTheme {
        Scaffold(
            bottomBar = {
                Box(
                    modifier = Modifier
                        .height(80.dp)
                        .fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.vector_1789),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row {
                            IconButton(onClick = { selected = 0 }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.home),
                                    contentDescription = stringResource(R.string.home),
                                    tint = if (selected == 0) MaterialTheme.colorScheme.primary else Color.Black
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = { selected = 1 }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.favorite),
                                    contentDescription = stringResource(R.string.favourite),
                                    tint = if (selected == 1) MaterialTheme.colorScheme.primary else Color.Black
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .offset(y = (-20).dp)
                                .size(56.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            FloatingActionButton(
                                onClick = { onCartClick() },
                                modifier = Modifier.size(56.dp),
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                shape = CircleShape
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.bag_2),
                                    contentDescription = stringResource(R.string.cart),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Row {
                            IconButton(onClick = { selected = 2 }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.notification),
                                    contentDescription = stringResource(R.string.notifications),
                                    tint = if (selected == 2) MaterialTheme.colorScheme.primary else Color.Black
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = { selected = 3 }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.profile),
                                    contentDescription = stringResource(R.string.profile),
                                    tint = if (selected == 3) MaterialTheme.colorScheme.primary else Color.Black
                                )
                            }
                        }
                    }
                }
            },
            containerColor = Color(0xFFF7F7F9)
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(Color(0xFFF7F7F9))
            ) {
                if (selected == 0) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.home),
                            style = AppTypography.headingRegular32,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            textAlign = TextAlign.Center
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White)
                            ) {
                                OutlinedTextField(
                                    value = "",
                                    onValueChange = {},
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    placeholder = {
                                        Text(
                                            text = stringResource(id = R.string.search),
                                            style = AppTypography.bodyRegular14
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = stringResource(R.string.search),
                                            tint = Color.Gray
                                        )
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent,
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                                    .clickable { onSettingsClick() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.sliders),
                                    contentDescription = stringResource(R.string.settings),
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            item {
                                CategorySection(
                                    categories = categories,
                                    selectedCategory = selectedCategory,
                                    onCategorySelected = { categoryName ->
                                        selectedCategory = categoryName
                                    }
                                )
                            }

                            item {
                                PopularSection(
                                    products = popularProducts,
                                    onProductClick = onProductClick,
                                    onFavoriteClick = { product -> },
                                    onOpenCatalog = onOpenCatalog
                                )
                            }

                            item {
                                PromotionsSection()
                            }
                        }
                    }
                } else if (selected == 1) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.favourite),
                            style = AppTypography.headingRegular32
                        )
                    }
                } else if (selected == 2) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.notifications),
                            style = AppTypography.headingRegular32
                        )
                    }
                } else if (selected == 3) {
                    ProfileScreen(
                        onEditClick = onProfileEditClick,
                        onBackClick = { selected = 0 },
                        onLogoutClick = onProfileLogoutClick
                    )
                }
            }
        }
    }
}

@Composable
private fun CategorySection(
    categories: List<Category>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Column {
        Text(
            text = stringResource(id = R.string.categories),
            style = AppTypography.bodyMedium16,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                CategoryChip(
                    category = category.name,
                    isSelected = selectedCategory == category.name,
                    onClick = { onCategorySelected(category.name) }
                )
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
            .clickable { onClick() }
            .clip(RoundedCornerShape(16.dp)),
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

@Composable
private fun PopularSection(
    products: List<Product>,
    onProductClick: (Product) -> Unit,
    onFavoriteClick: (Product) -> Unit,
    onOpenCatalog: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.popular),
                style = AppTypography.bodyMedium16
            )
            Text(
                text = stringResource(id = R.string.all),
                style = AppTypography.bodyRegular12,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onOpenCatalog() }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        if (products.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(products) { product ->
                    ProductCard(
                        product = product,
                        onProductClick = { onProductClick(product) },
                        onFavoriteClick = { onFavoriteClick(product) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PromotionsSection() {
    Column {
        Text(
            text = stringResource(id = R.string.sales),
            style = AppTypography.bodyMedium16,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.frame_1000000849),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.FillWidth
        )
    }
}
