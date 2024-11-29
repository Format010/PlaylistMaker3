package com.example.playlistmaker2.media.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.format.DateTimeFormatter

@Entity(tableName = "track_playlist_table")
data class TrackPlaylistEntity(
    @PrimaryKey val idTrack: String,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artwork: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val urlTrack: String,
    val dataSort: String = DateTimeFormatter.ISO_INSTANT.format(Instant.now()),

)
