package com.example.shoestore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.shoestore.ui.screens.RegisterAccountScreen // Импортируем ваш экран
import com.example.shoestore.ui.theme.ShoeStoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoeStoreTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    RegisterAccountScreen(
                        modifier = Modifier.padding(innerPadding),
                        onNavigateToSignIn = {
                            this@MainActivity.finish()
                        }
                    )
                }
            }
        }
    }
}
