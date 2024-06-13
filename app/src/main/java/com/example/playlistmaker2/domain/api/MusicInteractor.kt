package com.example.playlistmaker2.domain.api


import com.example.playlistmaker2.domain.models.Track



interface MusicInteractor {
    fun searchMusic(expression: String, consumer: MusicConsumer)

    interface MusicConsumer {
        fun consume(foundMusic: List<Track>)
    }
}