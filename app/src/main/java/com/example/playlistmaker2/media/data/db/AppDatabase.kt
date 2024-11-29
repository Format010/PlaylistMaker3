package com.example.playlistmaker2.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker2.media.data.db.dao.FavouritesMediaDao
import com.example.playlistmaker2.media.data.db.dao.PlaylistDao
import com.example.playlistmaker2.media.data.db.dao.TrackPlaylistDao
import com.example.playlistmaker2.media.data.db.entity.FavouritesMediaEntity
import com.example.playlistmaker2.media.data.db.entity.PlaylistEntity
import com.example.playlistmaker2.media.data.db.entity.TrackPlaylistEntity


@Database(version = 1, entities = [FavouritesMediaEntity::class, PlaylistEntity::class, TrackPlaylistEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun favouritesMediaDao(): FavouritesMediaDao
    abstract fun trackPlaylistDao(): TrackPlaylistDao
    abstract fun playlistDao(): PlaylistDao
}