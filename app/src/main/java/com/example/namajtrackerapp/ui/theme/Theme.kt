package com.example.namajtrackerapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.example.namajtrackerapp.model.AppThemeMode

private val DarkColorScheme = darkColorScheme(
    primary = PeachPrimary,
    onPrimary = InkberryBackground,
    primaryContainer = ClayBrownDarkAccent,
    onPrimaryContainer = DarkTextPrimary,
    secondary = ClayBrownDarkAccent,
    onSecondary = DarkTextPrimary,
    secondaryContainer = InkberrySurfaceVariant,
    onSecondaryContainer = PeachPrimary,
    tertiary = WarmGold,
    onTertiary = InkberryBackground,
    background = InkberryBackground,
    onBackground = DarkTextPrimary,
    surface = InkberrySurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = InkberrySurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorder,
    outlineVariant = InkberrySurfaceVariant
)

private val LightColorScheme = lightColorScheme(
    primary = ClayBrownPrimary,
    onPrimary = LightSurface,
    primaryContainer = ClayBrownLight,
    onPrimaryContainer = ClayBrownDark,
    secondary = ClayBrownVariant,
    onSecondary = LightSurface,
    secondaryContainer = SoftButterAccent,
    onSecondaryContainer = LightTextPrimary,
    tertiary = WarmGold,
    onTertiary = LightSurface,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightTextSecondary,
    outline = LightBorder,
    outlineVariant = LightSurfaceVariant
)

@Composable
fun NamazTrackerTheme(
    themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val isDark = when (themeMode) {
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
        AppThemeMode.LIGHT -> false
        AppThemeMode.DARK -> true
    }

    val colorScheme = if (isDark) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

// Backward compatibility alias for AgonAppTheme
@Composable
fun AgonAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    NamazTrackerTheme(
        themeMode = if (darkTheme) AppThemeMode.DARK else AppThemeMode.LIGHT,
        content = content
    )
}
