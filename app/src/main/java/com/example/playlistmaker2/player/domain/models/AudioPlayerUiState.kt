package com.example.playlistmaker2.player.domain.models

import com.example.playlistmaker2.player.ui.BottomSheetState

data class AudioPlayerUiState(
    val currentPosition: Int = 0,
    val isPlaying: Boolean = false,
    val audioPlayerStateStatus: AudioPlayerStateStatus? = AudioPlayerStateStatus.DEFAULT,
    val onCompletionListener: Boolean = false,
    val favouritesTrack: Boolean = false,
    val playlistState: BottomSheetState = BottomSheetState.Empty,
    val insertTrackState: BottomSheetState = BottomSheetState.EmptyTrack,
    )
