package com.example.playlistmaker2

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker2.player.data.AudioPlayerRepository
import com.example.playlistmaker2.player.data.impl.AudioPlayerRepositoryImpl
import com.example.playlistmaker2.player.domain.AudioPlayerInteractor
import com.example.playlistmaker2.player.domain.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker2.search.ui.SearchViewModel
import com.example.playlistmaker2.util.CreatorSharing

const val NAME_SHARED_FILE = "settings"
const val SECRET_KEY = "wd"
const val USER_KEY_HISTORY = "history_save"
const val DELAY = 400L
const val CLICK_DEBOUNCE_DELAY = 1000L
const val AUDIO_PLAYER_DATA = "track"
const val SEARCH_DEBOUNCE_DELAY = 2000L
const val EDITED_TEXT = "KEY"
const val HISTORY_KEY = "wd"


class App: Application() {
    companion object {
        lateinit var sharedPrefs: SharedPreferences
    }
    private var darkTheme = false
    var searchViewModel: SearchViewModel? = null
    override fun onCreate() {
        super.onCreate()

        sharedPrefs = getSharedPreferences(NAME_SHARED_FILE, MODE_PRIVATE)
        CreatorSharing.initApplication(this)
        CreatorSharing.initialize(sharedPrefs)

        if (checkDarkThemeOnDevice()) darkTheme = sharedPrefs.getBoolean(SECRET_KEY, true)
        else darkTheme = sharedPrefs.getBoolean(SECRET_KEY, false)

        switchTheme(darkTheme)

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

//AudioPlayerInteractor
    private fun provideAudioPlayerRepository(): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl()
    }

    fun providePlayerInteractor(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(provideAudioPlayerRepository())
    }

}