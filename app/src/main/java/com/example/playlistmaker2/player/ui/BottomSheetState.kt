package com.example.playlistmaker2.player.ui

import com.example.playlistmaker2.media.domain.model.Playlist

sealed interface BottomSheetState {

        object Loading : BottomSheetState

        data class Content(
            var song: List<Playlist>,
        ) : BottomSheetState

    data class IsertTrack(
        var platListName: String,
        var result: Boolean
    ) : BottomSheetState

        object EmptyTrack: BottomSheetState

        object Empty: BottomSheetState



}