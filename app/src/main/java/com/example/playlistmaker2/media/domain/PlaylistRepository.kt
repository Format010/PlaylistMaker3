package com.example.playlistmaker2.media.domain

import com.example.playlistmaker2.media.domain.model.Playlist
import com.example.playlistmaker2.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun addNewPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun update(playlist: Playlist)
    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)
    suspend fun checkTrackInPlaylist(trackId: String, playlist: Playlist): Boolean
    fun getAllPlaylist(): Flow<List<Playlist>>
    fun getTrackListById(trackId: List<String>): Flow<List<Track>>
    suspend fun deleteTrackFromTable(trackId: String)
    fun getAllPlaylistbyTrackId(trackId: String): Flow<List<Playlist>>
    suspend fun delTrackIdToPlaylist(trackId: String, playlist: Playlist)
    suspend fun getPLaylistById(idPlaylist: Int): Playlist
}


