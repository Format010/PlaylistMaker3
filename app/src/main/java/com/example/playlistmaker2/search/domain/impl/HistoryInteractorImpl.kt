package com.example.playlistmaker2.search.domain.impl


import com.example.playlistmaker2.search.data.HistoryRepository
import com.example.playlistmaker2.search.domain.HistoryInteractor
import com.example.playlistmaker2.search.domain.model.Track


class HistoryInteractorImpl(
    private val repository: HistoryRepository
) : HistoryInteractor {

    override fun read(): List<Track> {
        return repository.read()
    }

    override fun write(searchHistory: List<Track>) {
        repository.write(searchHistory)
    }

    override fun clearSearch() {
        repository.clearSearch()
    }

    override fun addTrackToHistory(searchHistory: List<Track>, track: Track) {
        repository.addTrackToHistory(searchHistory, track)
    }
}