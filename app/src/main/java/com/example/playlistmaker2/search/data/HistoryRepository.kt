package com.example.playlistmaker2.search.data

import com.example.playlistmaker2.search.domain.model.Track

interface HistoryRepository {

    fun read(): List<Track>
    fun write(searchHistory: List<Track>)
    fun clearSearch()
    fun addTrackToHistory(searchHistory: List<Track>, track: Track)
}