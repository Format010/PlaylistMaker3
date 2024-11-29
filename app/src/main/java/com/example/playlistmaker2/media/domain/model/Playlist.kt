package com.example.playlistmaker2.media.domain.model

data class Playlist(
    val playlistId: Int = 0,
    val title: String,
    val description: String?,
    val filePath: String?,
    val trackId: String?,
    val trackCount: Int?
)