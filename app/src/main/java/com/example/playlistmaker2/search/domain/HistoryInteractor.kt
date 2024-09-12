package com.example.playlistmaker2.search.domain

import android.content.SharedPreferences
import com.example.playlistmaker2.search.domain.model.Track
import java.util.LinkedList

interface HistoryInteractor {

    fun read(): LinkedList<Track>
    fun write(searchHistory: LinkedList<Track>)
    fun clearSearch()
    fun addTrackToHistory(searchHistory: LinkedList<Track>, track: Track)
}