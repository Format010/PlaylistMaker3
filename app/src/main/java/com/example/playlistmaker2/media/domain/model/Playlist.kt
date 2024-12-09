package com.example.playlistmaker2.media.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val playlistId: Int = 0,
    val title: String,
    val description: String?,
    val filePath: String?,
    val trackId: String?,
    val trackCount: Int?
) : Parcelable