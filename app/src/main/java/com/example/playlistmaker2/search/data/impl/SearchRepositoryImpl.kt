package com.example.playlistmaker2.search.data.impl

import com.example.playlistmaker2.search.data.NetworkClient
import com.example.playlistmaker2.search.data.dto.SearchRequest
import com.example.playlistmaker2.search.data.dto.SearchResponse
import com.example.playlistmaker2.search.data.SearchRepository
import com.example.playlistmaker2.search.domain.model.Track
import com.example.playlistmaker2.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(private val networkClient: NetworkClient) : SearchRepository {

    override fun searchMusic(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(SearchRequest(expression))

        when (response.code) {
            -1 -> {
                emit(Resource.Error("Not connect internet"))
            }

            0 -> {
                emit(Resource.Error("Is empty"))
            }

            200 -> {
                emit(Resource.Success((response as SearchResponse).results.map {
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
                }))
            }

            404 -> {
                emit(Resource.Error("404"))
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }

        }

    }

}