package com.example.playlistmaker2.search.domain

import com.example.playlistmaker2.search.domain.model.Track


interface HistoryInteractor {

    fun read(): List<Track>
    fun write(searchHistory: List<Track>)
    fun clearSearch()
    fun addTrackToHistory(searchHistory: List<Track>, track: Track)
}