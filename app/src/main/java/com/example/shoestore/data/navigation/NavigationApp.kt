package com.example.shoestore.data.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.shoestore.ui.screens.* // Импортируем все экраны

@Composable
fun NavigationApp(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "start_menu"
    ) {
        composable("start_menu") {
            OnboardScreen(
                onGetStartedClick = { navController.navigate("sign_up") },
            )
        }

        composable("sign_up") {
            RegisterAccountScreen(
                onSignInClick = { navController.navigate("sign_in") },
                onSignUpClick = { navController.navigate("email_verification") }
            )
        }

        composable("sign_in") {
            SignInScreen(
                onForgotPasswordClick = { navController.navigate("forgot_password") },
                onSignUpClick = { navController.navigate("sign_up") },
                onSignInClick = {
                    navController.navigate("home") {
                        // Очищаем стек, чтобы нельзя было вернуться на экран входа
                        popUpTo("sign_in") { inclusive = true }
                    }
                }
            )
        }

        composable("forgot_password") {
            ForgotPasswordScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToOTP = { email -> navController.navigate("otp/$email") }
            )
        }

        composable("otp/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            VerificationScreen(
                email = email,
                onVerifySuccess = { navController.navigate("new_password") }
            )
        }

        composable("new_password") {
            CreateNewPasswordScreen(
                onBackClick = { navController.popBackStack() },
                onSuccessNavigation = {
                    navController.navigate("sign_in") {
                        popUpTo("sign_in") { inclusive = true }
                    }
                }
            )
        }

        composable("email_verification") {
            EmailVerificationScreen(
                onSignInClick = { navController.navigate("sign_in") },
                onVerificationSuccess = { navController.navigate("home") }
            )
        }

        // ЭКРАН HOME (Пункт 9)
        composable("home") {
            HomeScreen(
                onProductClick = { product ->
                    // Здесь будет переход на детали товара: navController.navigate("details/${product.id}")
                },
                onCartClick = { /* Переход в корзину */ },
                onSearchClick = { /* Логика поиска */ },
                onSettingsClick = {
                    // Переход в профиль (связываем меню с экраном профиля)
                    navController.navigate("profile")
                }
            )
        }

        // ЭКРАН PROFILE (Пункт 12)
        composable("profile") {
            ProfileScreen()
        }
    }
}