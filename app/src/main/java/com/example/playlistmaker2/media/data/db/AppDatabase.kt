package com.example.playlistmaker2.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker2.media.data.db.dao.FavouritesMediaDao
import com.example.playlistmaker2.media.data.db.entity.FavouritesMediaEntity

@Database(version = 1, entities = [FavouritesMediaEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun favouritesMediaDao(): FavouritesMediaDao
}