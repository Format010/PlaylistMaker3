package com.example.playlistmaker2.data

import com.example.playlistmaker2.data.dto.MusicSearchRequest
import com.example.playlistmaker2.data.dto.MusicSearchResponse
import com.example.playlistmaker2.domain.api.MusicRepository
import com.example.playlistmaker2.domain.models.Track


class MusicRepositoryImpl(private val networkClient: NetworkClient): MusicRepository {

    override fun searchMusic(expression: String): List<Track> {
        val response = networkClient.doRequest(MusicSearchRequest(expression))

        if (response.resultCode == 200) {
            return (response as MusicSearchResponse).results.map {//преобразуем список TrackDto в Track
                Track(it.trackId, it.trackName, it.artistName, it.trackTimeMillis, it.artworkUrl100, it.collectionName, it.releaseDate, it.primaryGenreName, it.country, it.previewUrl)
            }

        } else {
            return emptyList()
        }
    }

}