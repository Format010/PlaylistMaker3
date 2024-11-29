package com.example.playlistmaker2.player.ui

sealed interface InsertTrackState {

    data class IsertTrack(
        var platListName: String,
        var result: Boolean
    ) : InsertTrackState

    object Empty : InsertTrackState

}