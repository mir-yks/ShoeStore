package com.example.shoestore.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoestore.R
import com.example.shoestore.ui.theme.AppTypography
import com.example.shoestore.ui.theme.ShoeShopTheme
import com.example.shoestore.ui.viewmodel.DetailsViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    productId: String,
    onBackClick: () -> Unit,
    onCartClick: () -> Unit,
    viewModel: DetailsViewModel = viewModel()
) {
    val product by viewModel.product.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    ShoeShopTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            stringResource(R.string.app_name),
                            style = AppTypography.headingRegular32.copy(fontSize = 16.sp)
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .padding(8.dp)
                                .background(Color.White, CircleShape)
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back_button))
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = onCartClick,
                            modifier = Modifier
                                .padding(8.dp)
                                .background(Color.White, CircleShape)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.bag_2),
                                contentDescription = stringResource(R.string.cart),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFFF7F7F9))
                )
            },
            containerColor = Color(0xFFF7F7F9)
        ) { padding ->
            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                product?.let { item ->
                    Column(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Column(Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
                                Text(
                                    text = item.name,
                                    style = AppTypography.headingRegular32.copy(fontSize = 26.sp, fontWeight = FontWeight.Bold),
                                    modifier = Modifier.width(250.dp)
                                )
                                Text(
                                    text = item.category,
                                    style = AppTypography.bodyRegular16.copy(color = Color.Gray),
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                                Text(
                                    text = item.price,
                                    style = AppTypography.headingRegular32.copy(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }

                            val images = List(3) { item.imageResId ?: R.drawable.nike_zoom_winflo_3_831561_001_mens_running_shoes_11550187236tiyyje6l87_prev_ui_3 }
                            val pagerState = rememberPagerState()

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                HorizontalPager(
                                    count = images.size,
                                    state = pagerState,
                                    contentPadding = PaddingValues(horizontal = 32.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) { page ->
                                    val scale = if (pagerState.currentPage == page) 1.0f else 0.85f
                                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        Image(
                                            painter = painterResource(id = images[page]),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(250.dp)
                                                .graphicsLayer {
                                                    scaleX = scale
                                                    scaleY = scale
                                                },
                                            contentScale = ContentScale.Fit
                                        )
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                images.forEachIndexed { index, resId ->
                                    val isSelected = pagerState.currentPage == index
                                    Box(
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp)
                                            .size(56.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(if (isSelected) Color.White else Color(0xFFF7F7F9))
                                            .border(
                                                width = if (isSelected) 1.dp else 0.dp,
                                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                                shape = RoundedCornerShape(12.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painter = painterResource(resId),
                                            contentDescription = null,
                                            modifier = Modifier.size(40.dp)
                                        )
                                    }
                                }
                            }

                            var isExpanded by remember { mutableStateOf(false) }
                            Column(Modifier.padding(horizontal = 20.dp)) {
                                Text(
                                    text = item.description.ifEmpty { stringResource(R.string.no_description) },
                                    style = AppTypography.bodyRegular14.copy(color = Color.Gray),
                                    maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.animateContentSize()
                                )

                                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                                    Text(
                                        text = if (isExpanded) stringResource(R.string.hide) else stringResource(R.string.read_more),
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .clickable { isExpanded = !isExpanded }
                                            .padding(vertical = 8.dp)
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF7F7F9))
                                .padding(horizontal = 20.dp, vertical = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE5E5E5))
                                    .clickable { },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FavoriteBorder,
                                    contentDescription = stringResource(R.string.favourite),
                                    tint = Color.Black
                                )
                            }

                            Spacer(modifier = Modifier.width(18.dp))

                            Button(
                                onClick = { },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.bag_2),
                                    contentDescription = null,
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(stringResource(R.string.add_to_cart), color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
