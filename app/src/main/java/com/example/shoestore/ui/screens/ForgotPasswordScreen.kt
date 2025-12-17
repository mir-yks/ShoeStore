package com.example.shoestore.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoestore.ui.components.BackButton
import com.example.shoestore.ui.components.DisableButton
import com.example.shoestore.ui.theme.*
import com.example.shoestore.ui.viewmodel.ForgotPasswordState
import com.example.shoestore.ui.viewmodel.ForgotPasswordViewModel

@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit,
    onNavigateToOTP: (String) -> Unit,
    viewModel: ForgotPasswordViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (state is ForgotPasswordState.Success) {
            showDialog = true
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Background).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            BackButton(onClick = onBackClick)
        }

        Spacer(modifier = Modifier.height(40.dp))
        Text("Забыл пароль", style = AppTypography.headingRegular32)
        Text(
            "Введите свою учетную запись \nдля сброса пароля",
            style = AppTypography.bodyRegular16,
            color = SubtextDark,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 40.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it; viewModel.resetState() },
            placeholder = { Text("xyz@gmail.com") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(40.dp))

        if (state is ForgotPasswordState.Loading) {
            CircularProgressIndicator(color = Accent)
        } else {
            DisableButton(
                text = "Отправить",
                onClick = {
                    if (email.contains("@")) {
                        viewModel.sendResetCode(email)
                    }
                },
                enabled = email.isNotEmpty()
            )
        }

        if (state is ForgotPasswordState.Error) {
            Text((state as ForgotPasswordState.Error).message, color = Red)
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    onNavigateToOTP(email)
                }) { Text("ОК") }
            },
            title = { Text("Проверьте почту") },
            text = { Text("Код восстановления отправлен на $email") }
        )
    }
}