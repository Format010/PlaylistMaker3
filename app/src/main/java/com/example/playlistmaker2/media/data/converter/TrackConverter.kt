package com.example.playlistmaker2.media.data.converter

import com.example.playlistmaker2.media.data.db.entity.FavouritesMediaEntity
import com.example.playlistmaker2.search.domain.model.Track

class TrackConverter {

    fun mapTrackToEntity(track: Track?): FavouritesMediaEntity {
        return FavouritesMediaEntity(
            track?.trackId?: "",
            track?.trackName?: "",
            track?.artistName?: "",
            track?.trackTimeMillis?: "",
            track?.artworkUrl100?: "",
            track?.collectionName?: "",
            track?.releaseDate?: "",
            track?.primaryGenreName?: "",
            track?.country?: "",
            track?.previewUrl?: "",

            )
    }

    fun mapEntityToTrack(track: FavouritesMediaEntity): Track {
        return Track(
            track.id,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artwork,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.urlTrack,

            )
    }
}