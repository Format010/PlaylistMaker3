package com.example.playlistmaker2.media.ui.playlist

import com.example.playlistmaker2.media.domain.model.Playlist

sealed interface PlaylistState {
    object Loading : PlaylistState

    data class Content(
        val playlists: List<Playlist>
    ) : PlaylistState

    data class Empty(
        val message: String
    ) : PlaylistState
}