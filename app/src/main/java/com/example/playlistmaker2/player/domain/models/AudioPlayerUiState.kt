package com.example.playlistmaker2.player.domain.models

data class AudioPlayerUiState(
    val currentPosition: Int = 0,
    val isPlaying: Boolean = false,
    val audioPlayerStateStatus: AudioPlayerStateStatus? = AudioPlayerStateStatus.DEFAULT,
    val onCompletionListener: Boolean = false,
    val favouritesTrack: Boolean = false,
    )
