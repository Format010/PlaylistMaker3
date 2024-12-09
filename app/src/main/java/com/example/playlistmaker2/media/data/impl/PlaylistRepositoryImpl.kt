package com.example.playlistmaker2.media.data.impl

import com.example.playlistmaker2.media.data.converter.PlaylistConverter
import com.example.playlistmaker2.media.data.converter.TrackConverter
import com.example.playlistmaker2.media.data.db.dao.PlaylistDao
import com.example.playlistmaker2.media.data.db.dao.TrackPlaylistDao
import com.example.playlistmaker2.media.data.db.entity.PlaylistEntity
import com.example.playlistmaker2.media.data.db.entity.TrackPlaylistEntity
import com.example.playlistmaker2.media.domain.PlaylistRepository
import com.example.playlistmaker2.media.domain.model.Playlist
import com.example.playlistmaker2.search.domain.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

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
        val updateTrackId = gson.toJson(trackId)
        val updatePlaylist = playlist.copy(
            trackId = updateTrackId,
            trackCount = trackId.size
        )
        playlistDao.update(playlistConverter.mapPlaylistToEntity(updatePlaylist))
        trackPlaylistDao.insertTrack(playlistConverter.mapTrackToEntityPlaylist(track))
    }

    override suspend fun delTrackIdToPlaylist(trackId: String, playlist: Playlist) {
        val trackIdList: MutableList<Int> = gson.fromJson(playlist.trackId, object :
            TypeToken<List<Int>>() {}.type)
        trackIdList.remove(trackId.toInt())
        val updateTrackId = gson.toJson(trackIdList)
        val updatePlaylist = playlist.copy(
            trackId = updateTrackId,
            trackCount = trackIdList.size
        )
        playlistDao.update(playlistConverter.mapPlaylistToEntity(updatePlaylist))

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

    override fun getTrackListById(trackId: List<String>): Flow<List<Track>> {
       return trackPlaylistDao.getTracksById(trackId).map { entities ->
           playlistConverter.mapEntityPlaylistToTrack(entities) }
    }

    override suspend fun deleteTrackFromTable(trackId: String) {
        trackPlaylistDao.deleteTrackFromTable(trackId)
    }

    override fun getAllPlaylistbyTrackId(trackId: String): Flow<List<Playlist>> {
        return playlistDao.getAllPlaylistsByTrackId(trackId).map {
            convertFromPlaylistEntity(it)
        }
    }

    override suspend fun getPLaylistById(idPlaylist: Int): Playlist {
        return playlistConverter.mapEntityToPlaylist(playlistDao.getPlaylistById(idPlaylist))



    }

}