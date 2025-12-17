package com.example.shoestore.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.example.shoestore.R

private val fontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

private val ralewayName = GoogleFont("Raleway")

val ralewayRegular = FontFamily(
    Font(
        googleFont = ralewayName,
        fontProvider = fontProvider,
        weight = FontWeight.Normal
    )
)

val ralewayMedium = FontFamily(
    Font(
        googleFont = ralewayName,
        fontProvider = fontProvider,
        weight = FontWeight.Medium
    )
)

val ralewaySemiBold = FontFamily(
    Font(
        googleFont = ralewayName,
        fontProvider = fontProvider,
        weight = FontWeight.SemiBold
    )
)

val ralewayBold = FontFamily(
    Font(
        googleFont = ralewayName,
        fontProvider = fontProvider,
        weight = FontWeight.Bold
    )
)

private val LightColorScheme = lightColorScheme(
    primary = Accent,
    secondary = Disable,
    surface = Block,
    background = Background,
    onBackground = Hint,
    onSurface = Text,
    onPrimary = Color.White,
    onSecondary = Color.White,
    error = Red,
    outline = SubtextDark
)

private val DarkColorScheme = darkColorScheme(
    primary = Accent,
    secondary = Disable,
    surface = DarkBlock,
    background = DarkBackground,
    onBackground = DarkText,
    onSurface = DarkText,
    onPrimary = Color.White,
    onSecondary = Color.White,
    error = Red,
    outline = SubtextDark
)

@Composable
fun ShoeShopTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}