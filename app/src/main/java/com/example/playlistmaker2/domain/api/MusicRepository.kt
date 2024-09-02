package com.example.playlistmaker2.domain.api


import com.example.playlistmaker2.domain.models.Track

interface MusicRepository {
    fun searchMusic(expression: String): List<Track>
}