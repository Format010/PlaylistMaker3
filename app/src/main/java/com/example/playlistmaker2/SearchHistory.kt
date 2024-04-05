package com.example.playlistmaker2

import android.content.SharedPreferences
import com.google.gson.Gson
import java.util.LinkedList

const val USER_KEY_HISTORY = "history_save"
const val NAME_HISTORY_FILE_PREFERENCE = "history"

class SearchHistory(val sharedPref: SharedPreferences)  {

    fun read(): LinkedList<Track> {
        val json = sharedPref.getString(USER_KEY_HISTORY, null) ?: return LinkedList()
        return Gson().fromJson(json, Array<Track>::class.java).toCollection(LinkedList())
    }

    fun write(searchHistory: LinkedList<Track>) {
        val json = Gson().toJson(searchHistory)
        sharedPref.edit()
            .putString(USER_KEY_HISTORY, json)
            .apply()
    }

    fun clearSearch() {
        write(LinkedList())
    }

    fun addTrackToHistory(searchHistory: LinkedList<Track>, track: Track){
            searchHistory.remove(track)
            searchHistory.addFirst(track)
           if (searchHistory.count() > 10) {
               searchHistory.removeLast()
                }
    write(searchHistory)
    }
}