package com.example.playlistmaker2.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker2.settings.data.SettingsRepository
import com.example.playlistmaker2.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker2.settings.domain.SettingsInteractor
import com.example.playlistmaker2.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker2.sharing.data.ExternalNavigator
import com.example.playlistmaker2.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker2.sharing.domain.SharingInteractor
import com.example.playlistmaker2.sharing.domain.impl.SharingInteractorImpl

object CreatorSharing {
    private lateinit var application: Application
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var settingsInteractor: SettingsInteractor

    private fun provideExternalNavigator(context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

    fun initApplication(application: Application) {
        this.application = application
    }

    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(provideExternalNavigator(application))
    }
    fun initialize(sharedPrefs: SharedPreferences) {
        settingsRepository = SettingsRepositoryImpl(sharedPrefs)
        settingsInteractor = SettingsInteractorImpl(settingsRepository)
    }

    fun provideSettingsInteractor() : SettingsInteractor {
        if (!this::settingsInteractor.isInitialized) {
            throw IllegalStateException("Creator is not initialized")
        }
        return settingsInteractor
    }


}