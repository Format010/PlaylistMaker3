package com.example.playlistmaker2.settings.ui

data class SettingsState(
    val isDarkThemeEnabled: Boolean = false,
    val shareText: String = "",
    val supportEmail: String = "",
    val userAgreementUrl: String = ""
)