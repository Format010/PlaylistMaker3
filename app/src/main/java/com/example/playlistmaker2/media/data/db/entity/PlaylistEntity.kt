package com.example.playlistmaker2.media.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int = 0,
    val title: String,
    val description: String?,
    val filePath: String?,
    val trackId: String = Gson().toJson(emptyList<Int>()),
    val trackCount: Int = 0
)