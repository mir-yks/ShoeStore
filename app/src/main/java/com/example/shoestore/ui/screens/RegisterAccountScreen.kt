package com.example.shoestore.ui.screens

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoestore.R
import com.example.shoestore.ui.components.BackButton
import com.example.shoestore.ui.components.DisableButton
import com.example.shoestore.data.model.SignUpRequest
import com.example.shoestore.ui.components.AlertDialogWithTwoButtons
import com.example.shoestore.ui.theme.AppTypography
import com.example.shoestore.ui.viewmodel.SignUpState
import com.example.shoestore.ui.viewmodel.SignUpViewModel

@Composable
fun RegisterAccountScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
    viewModel: SignUpViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var pendingSignUpRequest by remember { mutableStateOf<SignUpRequest?>(null) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val signUpState by viewModel.signUpState.collectAsStateWithLifecycle()

    val sharedPreferences = remember {
        context.getSharedPreferences("shoe_shop_prefs", Context.MODE_PRIVATE)
    }

    LaunchedEffect(signUpState) {
        when (signUpState) {
            is SignUpState.Success -> {
                saveUserDataToPreferences(sharedPreferences, name, email)
                onSignUpClick()
                viewModel.resetState()
            }
            is SignUpState.Error -> {
                val error = (signUpState as SignUpState.Error)
                errorMessage = error.message

                val showDialog = when {
                    error.message.contains("Too many requests", ignoreCase = true) -> true
                    error.message.contains("rate limit", ignoreCase = true) -> true
                    error.message.contains("network", ignoreCase = true) -> true
                    error.message.contains("invalid", ignoreCase = true) -> true
                    else -> true
                }

                if (showDialog) {
                    showErrorDialog = true
                } else {
                }
                viewModel.resetState()
            }
            else -> {}
        }
    }

    AlertDialogWithTwoButtons(
        showDialog = showErrorDialog,
        onDismissRequest = {
            showErrorDialog = false
            errorMessage = ""
        },
        onConfirm = {
            showErrorDialog = false
            errorMessage = ""
            pendingSignUpRequest = null
        },
        onCancel = {
            showErrorDialog = false
            errorMessage = ""
            pendingSignUpRequest = null
        },
        title = stringResource(R.string.details),
        message = errorMessage,
        cancelButtonText = stringResource(R.string.cancel),
    )

    val hintColor = MaterialTheme.colorScheme.onSurfaceVariant
    val borderColor = MaterialTheme.colorScheme.outline
    val checkboxBorderColor = MaterialTheme.colorScheme.outlineVariant

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(23.dp)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
    ) {
        BackButton(
            onClick = onBackClick
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.register),
                style = AppTypography.headingRegular32,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(id = R.string.details),
                style = AppTypography.subtitleRegular16,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(bottom = 40.dp)
            )
        }

        Text(
            text = stringResource(id = R.string.name),
            style = AppTypography.bodyMedium16.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = {
                Text(
                    "xxxxxxxx",
                    style = AppTypography.bodyRegular14,
                    color = hintColor
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            shape = MaterialTheme.shapes.medium,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = borderColor,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = hintColor,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedPlaceholderColor = hintColor,
                unfocusedPlaceholderColor = hintColor
            ),
            textStyle = AppTypography.bodyRegular16,
            singleLine = true
        )

        Text(
            text = stringResource(id = R.string.email),
            style = AppTypography.bodyMedium16.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = {
                Text(
                    "......@mail.com",
                    style = AppTypography.bodyRegular14,
                    color = hintColor
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = borderColor,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = hintColor,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedPlaceholderColor = hintColor,
                unfocusedPlaceholderColor = hintColor
            ),
            textStyle = AppTypography.bodyRegular16,
            singleLine = true
        )

        Text(
            text = stringResource(id = R.string.pass),
            style = AppTypography.bodyMedium16.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = {
                Text(
                    "......",
                    style = AppTypography.bodyRegular14,
                    color = hintColor
                )
            },
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            shape = MaterialTheme.shapes.medium,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = borderColor,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = hintColor,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedPlaceholderColor = hintColor,
                unfocusedPlaceholderColor = hintColor
            ),
            textStyle = AppTypography.bodyRegular16,
            singleLine = true,
            trailingIcon = {
                IconButton(
                    onClick = { passwordVisible = !passwordVisible }
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (passwordVisible) {
                                R.drawable.eye_close
                            } else {
                                R.drawable.eye_open
                            }
                        ),
                        contentDescription = if (passwordVisible) {
                            "Скрыть пароль"
                        } else {
                            "Показать пароль"
                        },
                        tint = hintColor
                    )
                }
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(MaterialTheme.shapes.small)
                    .selectable(
                        selected = isChecked,
                        onClick = { isChecked = !isChecked },
                        role = Role.Checkbox
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(MaterialTheme.shapes.small)
                        .border(
                            width = 2.dp,
                            color = if (isChecked) MaterialTheme.colorScheme.primary else checkboxBorderColor,
                            shape = MaterialTheme.shapes.small
                        )
                        .background(
                            if (isChecked) MaterialTheme.colorScheme.primary else Color.Transparent
                        )
                )

                if (isChecked) {
                    Icon(
                        painter = painterResource(id = R.drawable.policy_check),
                        contentDescription = "Выбрано",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = stringResource(id = R.string.agree),
                style = AppTypography.bodyRegular14,
                color = hintColor,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        DisableButton(
            text = stringResource(id = R.string.sign_up),
            onClick = {
                if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && isChecked) {
                    val signUpRequest = SignUpRequest(email, password)
                    pendingSignUpRequest = signUpRequest
                    viewModel.signUp(signUpRequest)
                } else {
                    errorMessage = when {
                        name.isEmpty() -> "Please enter your name"
                        email.isEmpty() -> "Please enter your email address"
                        password.isEmpty() -> "Please enter your password"
                        !isChecked -> "Please accept the terms and conditions"
                        else -> "Please fill in all required fields"
                    }
                    showErrorDialog = true
                }
            },
            enabled = name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && isChecked,
            textStyle = AppTypography.bodyMedium16
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(
                onClick = onSignInClick,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontFamily = AppTypography.bodyRegular16.fontFamily,
                                fontSize = AppTypography.bodyRegular16.fontSize
                            )
                        ) {
                            append(stringResource(id = R.string.have_acc))
                        }
                        append(" ")
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = AppTypography.bodyRegular16.fontFamily,
                                fontSize = AppTypography.bodyRegular16.fontSize,
                            )
                        ) {
                            append(stringResource(id = R.string.sign_in))
                        }
                    }
                )
            }
        }
    }
}

private fun saveUserDataToPreferences(
    sharedPreferences: SharedPreferences,
    name: String,
    email: String
) {
    sharedPreferences.edit {
        putString("user_name", name)
        putString("user_email", email)
    }
}