package com.example.playlistmaker2.settings.ui

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker2.settings.domain.SettingsInteractor
import com.example.playlistmaker2.sharing.domain.SharingInteractor
import com.example.playlistmaker2.sharing.domain.model.EmailData

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor,

) : ViewModel() {

    companion object {
        fun getViewModelFactory(settingsInteractor: SettingsInteractor, sharingInteractor: SharingInteractor, ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                // 1
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SettingsViewModel(
                        settingsInteractor, sharingInteractor,
                    ) as T
                }
            }
    }

    private val settingsState = MutableLiveData<SettingsState>()
    fun settingsState(): LiveData<SettingsState> = settingsState
    init {
        synchronizeTheme()
    }

    fun synchronizeTheme() {
        val getThemeSettings = settingsInteractor.getThemeSettings()
        settingsState.value = SettingsState(getThemeSettings)
        applyTheme(getThemeSettings)
    }

    fun setDarkTheme(enabled: Boolean) {
        settingsInteractor.updateThemeSetting(enabled)
        settingsState.value = settingsState.value?.copy(enabled)
        applyTheme(enabled)
    }

    fun shareApp(shareText: String) {
        sharingInteractor.shareApp(shareText)
    }

    fun contactSupport(data: EmailData) {
        sharingInteractor.sendMailSupport(data)
    }

    fun openUserAgreement(url: String) {
        sharingInteractor.openLinkUserAgreement(url)
    }

    private fun applyTheme(enabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (enabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

}