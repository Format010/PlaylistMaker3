package com.example.playlistmaker2.search.domain

import com.example.playlistmaker2.search.domain.model.Track

interface SearchInteractor {
    fun searchMusic(expression: String, consumer: MusicConsumer)

    interface MusicConsumer {
        fun consume(foundMusic: List<Track>?, errorMessage: String?)
    }
}