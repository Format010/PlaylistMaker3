package com.example.playlistmaker2.settings.domain

interface SettingsInteractor {

    fun getThemeSettings(): Boolean
    fun updateThemeSetting(settings: Boolean)

}