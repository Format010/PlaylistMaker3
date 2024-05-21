package com.example.playlistmaker2

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize

data class Track(
    val trackId: String?,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: String?,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
) : Parcelable {

    val artworkUrl512
        get() = artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

}