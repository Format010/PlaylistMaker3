package com.example.playlistmaker2.search.data.impl

import com.example.playlistmaker2.App
import com.example.playlistmaker2.USER_KEY_HISTORY
import com.example.playlistmaker2.search.data.HistoryRepository
import com.example.playlistmaker2.search.domain.model.Track
import com.google.gson.Gson
import java.util.LinkedList

class HistoryRepositoryImpl(): HistoryRepository {

    override fun read(): LinkedList<Track> {
        val json = App.sharedPrefs.getString(USER_KEY_HISTORY, null) ?: return LinkedList()
        return Gson().fromJson(json, Array<Track>::class.java).toCollection(LinkedList())
    }

    override fun write(searchHistory: LinkedList<Track>) {
        val json = Gson().toJson(searchHistory)
        App.sharedPrefs.edit()
            .putString(USER_KEY_HISTORY, json)
            .apply()
    }

    override fun clearSearch() {
        write(LinkedList())
    }

    override fun addTrackToHistory(searchHistory: LinkedList<Track>, track: Track) {
        searchHistory.remove(track)
        searchHistory.addFirst(track)
        if (searchHistory.count() > 10) {
            searchHistory.removeLast()
        }
        write(searchHistory)
    }
}

