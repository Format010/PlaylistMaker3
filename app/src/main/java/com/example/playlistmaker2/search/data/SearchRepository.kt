package com.example.playlistmaker2.search.data


import com.example.playlistmaker2.search.domain.model.Track
import com.example.playlistmaker2.util.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchMusic(expression: String): Flow<Resource<List<Track>>>
}