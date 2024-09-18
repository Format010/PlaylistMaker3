package com.example.playlistmaker2.search.data.impl

import com.example.playlistmaker2.search.data.NetworkClient
import com.example.playlistmaker2.search.data.dto.SearchRequest
import com.example.playlistmaker2.search.data.dto.SearchResponse
import com.example.playlistmaker2.search.data.SearchRepository
import com.example.playlistmaker2.search.domain.model.Track
import com.example.playlistmaker2.util.Resource


class SearchRepositoryImpl(private val networkClient: NetworkClient) : SearchRepository {

    override fun searchMusic(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(SearchRequest(expression))

        return when (response.code) {
            -1 -> {
                Resource.Error("Not connect internet")
            }

            200 -> {
                Resource.Success((response as SearchResponse).results.map {
                    Track(
                        it.trackId,
                        it.trackName,
                        it.artistName,
                        it.trackTimeMillis,
                        it.artworkUrl100,
                        it.collectionName,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl
                    )

                })

            }

            404 -> {
                Resource.Error("404")
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }

        }

    }

}