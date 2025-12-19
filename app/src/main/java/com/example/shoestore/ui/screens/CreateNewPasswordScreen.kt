package com.example.shoestore.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoestore.R
import com.example.shoestore.ui.components.BackButton
import com.example.shoestore.ui.components.DisableButton
import com.example.shoestore.ui.theme.*
import com.example.shoestore.ui.viewmodel.UpdatePasswordState
import com.example.shoestore.ui.viewmodel.UpdatePasswordViewModel

@Composable
fun CreateNewPasswordScreen(
    onBackClick: () -> Unit,
    onSuccessNavigation: () -> Unit,
    viewModel: UpdatePasswordViewModel = viewModel()
) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val uiState by viewModel.state.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is UpdatePasswordState.Success) {
            onSuccessNavigation()
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp)
    ) {
        Box(modifier = Modifier.padding(top = 20.dp)) {
            BackButton(onClick = onBackClick)
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Новый пароль",
            style = AppTypography.headingRegular32,
            color = Text
        )
        Text(
            text = "Пожалуйста, введите новый пароль",
            style = AppTypography.bodyRegular16,
            color = SubtextDark,
            modifier = Modifier.padding(top = 8.dp, bottom = 40.dp)
        )

        Text("Пароль", style = AppTypography.bodyMedium16, color = Text)
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = null
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        painter = painterResource(id = if (isPasswordVisible) R.drawable.eye_open else R.drawable.eye_close),
                        contentDescription = null,
                        tint = Hint
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Block,
                focusedContainerColor = Block,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Accent
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Повторите пароль", style = AppTypography.bodyMedium16, color = Text)
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                errorMessage = null
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            isError = errorMessage != null,
            visualTransformation = if (isConfirmVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isConfirmVisible = !isConfirmVisible }) {
                    Icon(
                        painter = painterResource(id = if (isConfirmVisible) R.drawable.eye_open else R.drawable.eye_close),
                        contentDescription = null,
                        tint = Hint
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Block,
                focusedContainerColor = Block,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Accent
            )
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Red,
                style = AppTypography.bodyRegular12,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        if (uiState is UpdatePasswordState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally), color = Accent)
        } else {
            DisableButton(
                text = "Сохранить",
                onClick = {
                    if (password.isEmpty() || confirmPassword.isEmpty()) {
                        errorMessage = "Поля не могут быть пустыми"
                    } else if (password != confirmPassword) {
                        errorMessage = "Пароли не совпадают"
                    } else {
                        viewModel.updatePassword(password)
                    }
                },
                textStyle = AppTypography.bodyMedium16
            )
        }

        if (uiState is UpdatePasswordState.Error) {
            Text(
                text = (uiState as UpdatePasswordState.Error).message,
                color = Red,
                modifier = Modifier.padding(top = 16.dp).align(Alignment.CenterHorizontally)
            )
        }
    }
}