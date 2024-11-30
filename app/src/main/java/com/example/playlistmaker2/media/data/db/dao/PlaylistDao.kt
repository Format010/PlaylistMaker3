package com.example.playlistmaker2.media.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker2.media.data.db.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Update
    suspend fun update(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlist_table WHERE playlistId = :playlistId")
    suspend fun getPlaylistById(playlistId: Int): PlaylistEntity


    @Query("UPDATE playlist_table SET trackId = :trackId, trackCount = :trackCount WHERE playlistId = :playlistId")
    suspend fun updateTrackId(playlistId: Int, trackId: String, trackCount: Int)

    @Query("SELECT * FROM playlist_table WHERE trackId LIKE :trackId")
    fun getAllPlaylistsByTrackId(trackId: String): Flow<List<PlaylistEntity>>

}