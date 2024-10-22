package com.example.playlistmaker2.media.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites_media_table")
data class FavouritesMediaEntity(
    @PrimaryKey @ColumnInfo(name = "media_id") val id: String,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artwork: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val urlTrack: String,

)
