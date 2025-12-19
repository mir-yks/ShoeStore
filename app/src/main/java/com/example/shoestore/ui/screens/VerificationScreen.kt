package com.example.shoestore.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoestore.ui.components.BackButton
import com.example.shoestore.ui.theme.AppTypography
import com.example.shoestore.ui.viewmodel.OTPState
import com.example.shoestore.ui.viewmodel.OTPViewModel
import kotlinx.coroutines.delay

@Composable
fun VerificationScreen(
    email: String,
    onVerifySuccess: () -> Unit,
    viewModel: OTPViewModel = viewModel()
) {
    var otpValue by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    // Таймер (ТЗ №24)
    var timeLeft by remember { mutableIntStateOf(30) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus() // Авто-фокус при входе
        while (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        }
    }

    LaunchedEffect(state) {
        if (state is OTPState.Success) {
            onVerifySuccess()
        }
    }

    val isError = state is OTPState.Error

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
            BackButton(onClick = { /* Навигация назад */ })
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "OTP Verification",
            style = AppTypography.headingRegular32,
            color = Color(0xFF2B2B2B)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Please check your email $email to see the verification code",
            style = AppTypography.subtitleRegular16,
            color = Color(0xFF707B81),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "OTP Code",
            modifier = Modifier.fillMaxWidth(),
            style = AppTypography.headingRegular32.copy(fontSize = 21.sp),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Поле ввода
        Box(contentAlignment = Alignment.Center) {
            BasicTextField(
                value = otpValue,
                onValueChange = {
                    if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                        otpValue = it
                        if (it.length == 6) {
                            viewModel.verifyCode(email, it)
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .alpha(0.01f) // Почти невидимый, но рабочий
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                repeat(6) { index ->
                    val char = otpValue.getOrNull(index)?.toString() ?: ""
                    val isFocused = otpValue.length == index

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF7F7F9))
                            .border(
                                width = 1.dp,
                                color = when {
                                    isError -> Color.Red
                                    isFocused -> MaterialTheme.colorScheme.primary
                                    else -> Color.Transparent
                                },
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            style = AppTypography.headingRegular32.copy(fontSize = 24.sp),
                            color = Color(0xFF2B2B2B)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Нижняя часть с таймером
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Resend code",
                style = AppTypography.bodyRegular12,
                color = Color(0xFF707B81)
            )
            Text(
                text = "00:${timeLeft.toString().padStart(2, '0')}",
                style = AppTypography.bodyRegular12,
                color = Color(0xFF707B81)
            )
        }

        if (state is OTPState.Loading) {
            Spacer(modifier = Modifier.height(20.dp))
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    }
}