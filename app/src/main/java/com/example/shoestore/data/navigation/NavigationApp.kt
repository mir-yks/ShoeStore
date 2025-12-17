package com.example.shoestore.data.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.shoestore.ui.screens.EmailVerificationScreen
import com.example.shoestore.ui.screens.HomeScreen
import com.example.shoestore.ui.screens.OnboardScreen
import com.example.shoestore.ui.screens.RegisterAccountScreen
import com.example.shoestore.ui.screens.SignInScreen

@Composable
fun NavigationApp(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "start_menu"
    ) {
        composable("sign_up") {
            RegisterAccountScreen(
                onSignInClick = { navController.navigate("sign_in") },
                onSignUpClick = { navController.navigate("email_verification") }
            )
        }
        composable("sign_in") {
            SignInScreen(
                onSignInClick = { navController.navigate("home") },
                onSignUpClick = { navController.navigate("sign_up") }
            )
        }

        composable("email_verification") {
            EmailVerificationScreen(
                onSignInClick = { navController.navigate("sign_in") },
                onVerificationSuccess = { navController.navigate("home") }
            )
        }

        composable("start_menu") {
            OnboardScreen (
                onGetStartedClick = { navController.navigate("sign_up") },
            )
        }

        composable("home") {
            HomeScreen({},{},{})
        }

    }
}