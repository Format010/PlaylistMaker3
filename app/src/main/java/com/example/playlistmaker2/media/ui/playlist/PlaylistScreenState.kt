package com.example.playlistmaker2.media.ui.playlist

import com.example.playlistmaker2.media.domain.model.Playlist
import com.example.playlistmaker2.search.domain.model.Track

sealed interface PlaylistScreenState {

        object Loading : PlaylistScreenState

        data class Content(
            val trackList: List<Track>,
            val playlist: Playlist
        ) : PlaylistScreenState

        object Delite : PlaylistScreenState

}