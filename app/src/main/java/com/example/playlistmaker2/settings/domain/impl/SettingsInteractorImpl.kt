package com.example.playlistmaker2.settings.domain.impl

import com.example.playlistmaker2.settings.data.SettingsRepository
import com.example.playlistmaker2.settings.domain.SettingsInteractor

class SettingsInteractorImpl(private val repository: SettingsRepository) :
    SettingsInteractor {
    override fun getThemeSettings(): Boolean {
        return repository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: Boolean) {
       repository.updateThemeSetting(settings)
    }
}