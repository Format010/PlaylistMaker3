package com.example.playlistmaker2.data.network

import com.example.playlistmaker2.data.NetworkClient
import com.example.playlistmaker2.data.dto.MusicSearchRequest
import com.example.playlistmaker2.data.dto.MusicResponse
import com.example.playlistmaker2.domain.api.MusicRepository
import com.example.playlistmaker2.domain.models.Track

class MusicRepositoryImpl(private val networkClient: NetworkClient): MusicRepository {

    override fun searchMusic(expression: String): List<Track> {
        val response = networkClient.doRequest(MusicSearchRequest(expression))

        if (response.resultCode == 200) {
            return (response as MusicResponse).results.map {
                Track(it.trackId, it.trackName, it.artistName, it.trackTimeMillis, it.artworkUrl100, it.collectionName, it.releaseDate, it.primaryGenreName, it.country, it.previewUrl)
            }

        } else {
            return emptyList()
        }
    }

}