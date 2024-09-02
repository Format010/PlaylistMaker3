package com.example.playlistmaker2.search.ui

import com.example.playlistmaker2.search.domain.model.Track



sealed interface SearchState {

    object Loading : SearchState

    data class Content(
        var song: List<Track>,
    ) : SearchState

    object Error: SearchState

    object Empty: SearchState

}
