package com.example.playlistmaker2.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker2.media.data.db.entity.TrackPlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackPlaylistDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackPlaylistEntity)

    @Query("DELETE FROM track_playlist_table Where idTrack = :trackId")
    suspend fun deleteTrackFromTable(trackId: String)

    @Query("SELECT * FROM track_playlist_table WHERE idTrack IN (:trackId)")
    fun getTracksById(trackId: List<String>): Flow<List<TrackPlaylistEntity>>


}