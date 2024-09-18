package com.example.playlistmaker2.player.domain.models

data class AudioPlayerUiState(
    var currentPosition: Int = 0,
    var isPlaying: Boolean = false,
    var audioPlayerStateStatus: AudioPlayerStateStatus? = AudioPlayerStateStatus.STATE_DEFAULT,

    )
