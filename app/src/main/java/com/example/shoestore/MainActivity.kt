package com.example.shoestore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.shoestore.data.navigation.NavigationApp
import com.example.shoestore.ui.theme.ShoeShopTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoeShopTheme {
                    val navController = rememberNavController()
                    NavigationApp(navController = navController)
                }
        }
    }
}
