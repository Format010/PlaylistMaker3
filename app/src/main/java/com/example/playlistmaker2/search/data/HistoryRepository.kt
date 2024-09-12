package com.example.playlistmaker2.search.data

import android.content.SharedPreferences
import com.example.playlistmaker2.search.domain.model.Track
import com.google.gson.Gson
import java.util.LinkedList

interface HistoryRepository {

    fun read(): LinkedList<Track>
    fun write(searchHistory: LinkedList<Track>)
    fun clearSearch()
    fun addTrackToHistory(searchHistory: LinkedList<Track>, track: Track)
}