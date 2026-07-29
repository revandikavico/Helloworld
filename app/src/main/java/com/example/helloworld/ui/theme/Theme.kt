package com.example.helloworld.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.compose.ui.graphics.Color

/**
 * MINGGU 1 — Tema Aplikasi dengan Material Design 3
 *
 * Material3 mendukung Dynamic Color (Android 12+) yang otomatis
 * menyesuaikan warna dengan wallpaper pengguna.
 */

// Skema warna Light Mode - Kustom: Latar Merah, Kartu Putih, Elemen Biru
private val LightColorScheme = lightColorScheme(
    primary = AppBlue,
    onPrimary = White,
    primaryContainer = AppBlueDark,
    onPrimaryContainer = White,
    secondary = AppBlue,
    onSecondary = White,
    background = AppRed,
    onBackground = White, // Teks di atas latar merah (Header)
    surface = White,      // Kartu dan input field menjadi putih agar mudah dibaca
    onSurface = DarkText, // Teks di dalam kartu/input menjadi gelap
    surfaceVariant = Color(0xFFF0F0F0),
    onSurfaceVariant = DarkText,
    secondaryContainer = AppBlue,
    onSecondaryContainer = White,
    outline = AppBlue
)

// Skema warna Dark Mode (Bisa disesuaikan, tapi sementara kita samakan untuk konsistensi permintaan)
private val DarkColorScheme = darkColorScheme(
    primary = AppBlue,
    onPrimary = White,
    primaryContainer = AppBlueDark,
    onPrimaryContainer = White,
    secondary = AppBlue,
    onSecondary = White,
    background = AppRed,
    onBackground = White,
    surface = White,
    onSurface = DarkText,
    surfaceVariant = Color(0xFFF0F0F0),
    onSurfaceVariant = DarkText,
    secondaryContainer = AppBlue,
    onSecondaryContainer = White,
    outline = AppBlue
)

@Composable
fun HelloWorldTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Diubah menjadi false agar warna kustom langsung terlihat
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Gunakan Dynamic Color jika tersedia (Android 12+)
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Update status bar color agar sesuai tema
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
