package com.example.playlistmaker2.data

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

const val NAME_SHARED_FILE = "settings"
const val SECRET_KEY = "wd"

class App: Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences(NAME_SHARED_FILE, MODE_PRIVATE)
        if (isDarkThemeOn()) {
            darkTheme = sharedPrefs.getBoolean(SECRET_KEY, true)
        }else {darkTheme = sharedPrefs.getBoolean(SECRET_KEY, false)}
        switchTheme(darkTheme)
    }
    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
    fun Context.isDarkThemeOn() = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}