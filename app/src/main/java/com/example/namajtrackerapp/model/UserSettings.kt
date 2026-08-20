package com.example.namajtrackerapp.model

import kotlinx.serialization.Serializable

@Serializable
enum class AppLanguage {
    ENGLISH,
    BANGLA
}

@Serializable
enum class AppThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

@Serializable
data class UserSettings(
    val userName: String = "Brother/Sister",
    val avatarIndex: Int = 0,
    val language: AppLanguage = AppLanguage.ENGLISH,
    val themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    val hasCompletedOnboarding: Boolean = false,
    val showSunnahPrayers: Boolean = true
)
