package com.example.shoestore.data.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.shoestore.ui.screens.*

@Composable
fun NavigationApp(
    navController: NavHostController,
    startDestination: String,
    onOnboardingFinished: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable("start_menu") {
            OnboardScreen(
                onGetStartedClick = {
                    onOnboardingFinished()
                    navController.navigate("sign_up") {
                        popUpTo("start_menu") { inclusive = true }
                    }
                }
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

        composable("home") {
            HomeScreen(
                onProductClick = { product ->
                },
                onCartClick = { /* ... */ },
                onSearchClick = { /* ... */ },
                onSettingsClick = { /* ... */ },
                onProfileEditClick = { navController.navigate("edit_profile") },
                onProfileLogoutClick = {
                    navController.navigate("sign_in") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onOpenCatalog = {
                    navController.navigate("catalog")
                }
            )
        }

        composable("profile") {
            ProfileScreen(
                onEditClick = { navController.navigate("edit_profile") },
                onBackClick = { navController.popBackStack() },
                onLogoutClick = {
                    navController.navigate("sign_in") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable("edit_profile") {
            EditProfileScreen(
                onBackClick = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }

        composable("catalog") {
            CatalogScreen(
                onProductClick = { product ->
                    navController.navigate("details/${product.id}")
                }
            )
        }
    }
}
