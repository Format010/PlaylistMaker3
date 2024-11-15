package com.example.playlistmaker2.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.playlistmaker2.media.data.db.entity.TrackPlaylistEntity

@Dao
interface TrackPlaylistDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackPlaylistEntity)
}