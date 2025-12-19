package com.example.shoestore

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.shoestore.data.navigation.NavigationApp
import com.example.shoestore.ui.theme.ShoeShopTheme

class MainActivity : ComponentActivity() {

    private val PREFS_NAME = "settings"
    private val KEY_ONBOARDING_COMPLETED = "onboarding_completed"

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val onboardingCompleted = prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)

        setContent {
            ShoeShopTheme {
                val navController = rememberNavController()
                NavigationApp(
                    navController = navController,
                    startDestination = if (onboardingCompleted) "sign_in" else "start_menu",
                    onOnboardingFinished = {
                        prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, true).apply()
                    }
                )
            }
        }
    }
}
