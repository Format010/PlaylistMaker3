package com.example.playlistmaker2

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize

data class Track(var trackId: String, val trackName: String, val artistName: String, var trackTimeMillis: String, val artworkUrl100: String, val collectionName: String, val valreleaseDate: String, val primaryGenreName: String, val country: String): Parcelable {


}