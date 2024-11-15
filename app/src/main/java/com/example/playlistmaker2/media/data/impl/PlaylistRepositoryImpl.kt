package com.example.playlistmaker2.media.data.impl

import com.example.playlistmaker2.media.data.converter.PlaylistConverter
import com.example.playlistmaker2.media.data.db.dao.PlaylistDao
import com.example.playlistmaker2.media.data.db.dao.TrackPlaylistDao
import com.example.playlistmaker2.media.data.db.entity.PlaylistEntity
import com.example.playlistmaker2.media.domain.PlaylistRepository
import com.example.playlistmaker2.media.domain.model.Playlist
import com.example.playlistmaker2.search.domain.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistConverter: PlaylistConverter,
    private val gson: Gson,
    private val trackPlaylistDao: TrackPlaylistDao,
    ) : PlaylistRepository {

    override suspend fun addNewPlaylist(playlist: Playlist) {
        val playlistEntity = playlistConverter.mapPlaylistToEntity(playlist)
        playlistDao.insertPlaylist(playlistEntity)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        val playlistEntity = playlistConverter.mapPlaylistToEntity(playlist)
        playlistDao.deletePlaylist(playlistEntity)
    }

    override suspend fun update(playlist: Playlist) {
        val playlistEntity = playlistConverter.mapPlaylistToEntity(playlist)
        playlistDao.update(playlistEntity)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        val trackId: MutableList<Int> = gson.fromJson(playlist.trackId, object :
            TypeToken<List<Int>>() {}.type)
        trackId.add(track.trackId?.toInt() ?: 0)
        val updatedTrackId = gson.toJson(trackId)
        val updatedPlaylist = playlist.copy(
            trackId = updatedTrackId,
            trackCount = trackId.size
        )
        playlistDao.update(playlistConverter.mapPlaylistToEntity(updatedPlaylist))
        trackPlaylistDao.insertTrack(playlistConverter.mapTrackToEntityPlaylist(track))
    }

    override suspend fun checkTrackInPlaylist(trackId: String, playlist: Playlist): Boolean {
        val trackId_: List<String> = gson.fromJson(playlist.trackId, object :
            TypeToken<List<String>>() {}.type)
        return trackId_.contains(trackId)
    }

    override fun getAllPlaylist(): Flow<List<Playlist>> {
        return playlistDao.getPlaylists().map {
            convertFromPlaylistEntity(it)
        }
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistConverter.mapEntityToPlaylist(playlist) }
        }

}