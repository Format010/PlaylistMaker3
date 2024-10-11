package com.example.playlistmaker2.search.domain

import com.example.playlistmaker2.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun searchMusic(expression: String): Flow<Pair<List<Track>?, String?>>
}