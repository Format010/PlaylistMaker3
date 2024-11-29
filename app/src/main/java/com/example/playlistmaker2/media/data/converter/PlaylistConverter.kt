package com.example.playlistmaker2.media.data.converter

import com.example.playlistmaker2.media.data.db.entity.PlaylistEntity
import com.example.playlistmaker2.media.data.db.entity.TrackPlaylistEntity
import com.example.playlistmaker2.media.domain.model.Playlist
import com.example.playlistmaker2.search.domain.model.Track
import com.google.gson.Gson

class PlaylistConverter {

    fun mapPlaylistToEntity(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.playlistId,
            playlist.title,
            playlist.description,
            playlist.filePath,
            playlist.trackId ?: Gson().toJson(emptyList<Int>()),
            playlist.trackCount ?: 0,
        )
    }

    fun mapEntityToPlaylist(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            playlistEntity.playlistId,
            playlistEntity.title,
            playlistEntity.description,
            playlistEntity.filePath,
            playlistEntity.trackId,
            playlistEntity.trackCount
        )
    }

    fun mapTrackToEntityPlaylist(track: Track): TrackPlaylistEntity{
        return TrackPlaylistEntity(
            track.trackId?: "",
            track?.trackName?: "",
            track.artistName?: "",
            track.trackTimeMillis?: "",
            track.artworkUrl512?: "",
            track.collectionName?: "",
            track.releaseDate?: "",
            track.primaryGenreName?: "",
            track.country?: "",
            track.previewUrl?: "",

        )
    }

    fun mapEntityPlaylistToTrack(entities: List<TrackPlaylistEntity>): List<Track> {
        return entities.map { entities -> Track(

            entities.idTrack?: "",
            entities.trackName?: "",
            entities.artistName?: "",
            entities.trackTime?: "",
            entities.artwork?: "",
            entities.collectionName?: "",
            entities.releaseDate?: "",
            entities.primaryGenreName?: "",
            entities.country?: "",
            entities.urlTrack?: "",

            ) }
    }
}