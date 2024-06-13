package com.example.playlistmaker2.data.dto

import android.os.Handler
import android.os.Looper

private const val DELAY = 500L

class ClickDelay {

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, DELAY)
        }
        return current
    }
}