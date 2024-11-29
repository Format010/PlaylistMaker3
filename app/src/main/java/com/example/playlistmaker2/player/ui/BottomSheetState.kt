package com.example.playlistmaker2.player.ui

import com.example.playlistmaker2.media.domain.model.Playlist

sealed interface BottomSheetState {

        object Loading : BottomSheetState

        data class Content(
            var song: List<Playlist>,
        ) : BottomSheetState

        object Empty: BottomSheetState



}