package com.example.playlistmaker2

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import audioPlayerModule
import mediaModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import searchModule
import settingsModule

const val SECRET_KEY = "wd"
const val USER_KEY_HISTORY = "history_save"
const val TIMER_MUSIC_DELAY = 300L
const val CLICK_DEBOUNCE_DELAY = 1000L
const val AUDIO_PLAYER_DATA = "track"
const val SEARCH_DEBOUNCE_DELAY = 2000L
const val EDITED_TEXT = "KEY"

class App: Application() {

    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(searchModule, settingsModule, audioPlayerModule, mediaModule)
        }

        val sharedPrefs = getSharedPreferences(SECRET_KEY, MODE_PRIVATE)
        darkTheme = if (checkDarkThemeOnDevice()) sharedPrefs.getBoolean(SECRET_KEY, true)
        else sharedPrefs.getBoolean(SECRET_KEY, false)
        switchTheme(darkTheme)
        sharedPrefs.edit {
            putBoolean(SECRET_KEY, darkTheme)
        }
    }

    private fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    private fun checkDarkThemeOnDevice() = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

}