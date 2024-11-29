package com.example.playlistmaker2.media.domain.impl

import android.adservices.adid.AdId
import com.example.playlistmaker2.media.domain.PlaylistInteractor
import com.example.playlistmaker2.media.domain.PlaylistRepository
import com.example.playlistmaker2.media.domain.model.Playlist
import com.example.playlistmaker2.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val repository: PlaylistRepository
) : PlaylistInteractor {
    override suspend fun addNewPlaylist(playlist: Playlist) {
        repository.addNewPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        repository.deletePlaylist(playlist)
    }

    override suspend fun update(playlist: Playlist) {
        repository.update(playlist)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        repository.addTrackToPlaylist(track, playlist)
    }

    override suspend fun checkTrackInPlaylist(trackId: String, playlist: Playlist): Boolean {
        return repository.checkTrackInPlaylist(trackId, playlist)
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return repository.getAllPlaylist()
    }

    override fun getTrackListById(trackId: List<String>): Flow<List<Track>>{
        return repository.getTrackListById(trackId)
    }

    override suspend fun deleteTrackFromTable(trackId: String) {
        repository.deleteTrackFromTable(trackId)
    }

    override fun getAllPlaylistbyTrackId(trackId: String): Flow<List<Playlist>> {
        return repository.getAllPlaylistbyTrackId(trackId)
            }

    override suspend fun delTrackIdToPlaylist(trackId: String, playlist: Playlist){
        repository.delTrackIdToPlaylist(trackId,playlist)
    }

    override suspend fun getPLaylistById(idPlaylist: Int): Playlist {
        return repository.getPLaylistById(idPlaylist)
    }

}