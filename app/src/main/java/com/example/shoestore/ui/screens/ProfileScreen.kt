package com.example.shoestore.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoestore.R
import com.example.shoestore.ui.viewmodels.ProfileViewModel
import com.example.shoestore.ui.viewmodels.ProfileUiState

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    val uiState = viewModel.uiState

    // Лаунчер для камеры
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let { viewModel.onPhotoCaptured(it) }
    }

    LaunchedEffect(Unit) { viewModel.loadProfile() }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Профиль", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.weight(1f))
                IconButton(onClick = { viewModel.isEditing = !viewModel.isEditing }) {
                    Icon(painterResource(id = R.drawable.edit), contentDescription = null)
                }
            }

            // Блок аватара с камерой
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .clickable { cameraLauncher.launch(null) } // Запуск камеры
            ) {
                if (viewModel.bitmapPhoto != null) {
                    Image(
                        bitmap = viewModel.bitmapPhoto!!.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        painterResource(id = R.drawable.profile), // замени на свою иконку
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            EditableField("Имя", viewModel.name, viewModel.isEditing) { viewModel.name = it }
            EditableField("Фамилия", viewModel.lastName, viewModel.isEditing) { viewModel.lastName = it }
            EditableField("Адрес", viewModel.address, viewModel.isEditing) { viewModel.address = it }
            EditableField("Телефон", viewModel.phone, viewModel.isEditing) { viewModel.phone = it }

            Spacer(modifier = Modifier.height(24.dp))

            if (viewModel.isEditing) {
                Button(
                    onClick = { viewModel.saveProfile() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Сохранить изменения") }
            }
        }

        if (uiState is ProfileUiState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        if (uiState is ProfileUiState.Error) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissError() },
                confirmButton = { TextButton(onClick = { viewModel.dismissError() }) { Text("OK") } },
                title = { Text("Ошибка") },
                text = { Text(uiState.message) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditableField(label: String, value: String, isEditable: Boolean, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(top = 16.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        if (isEditable) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color(0xFFF7F7F9), RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(text = value)
            }
        }
    }
}