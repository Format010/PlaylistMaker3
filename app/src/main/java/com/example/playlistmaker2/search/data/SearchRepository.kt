package com.example.playlistmaker2.search.data


import com.example.playlistmaker2.search.domain.model.Track
import com.example.playlistmaker2.util.Resource

interface SearchRepository {
    fun searchMusic(expression: String): Resource<List<Track>>
}