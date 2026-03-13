package com.example.soccmel.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BrightGold,
    secondary = TortellinoYellow,
    tertiary = Color.White,
    background = MidnightBlue,
    surface = DeepOceanBlue,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = BrightGold, // Soccmel e bottoni ora sono ORO ACCESO
    secondary = TortellinoYellow,
    tertiary = Color.White,
    background = MidnightBlue, 
    surface = DeepOceanBlue, 
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    primaryContainer = BrightGold,
    onPrimaryContainer = Color.Black
)

@Composable
fun SoccmelTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, 
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
