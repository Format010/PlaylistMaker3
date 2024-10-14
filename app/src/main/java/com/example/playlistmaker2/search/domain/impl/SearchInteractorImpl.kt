package com.example.playlistmaker2.search.domain.impl

import com.example.playlistmaker2.search.domain.SearchInteractor
import com.example.playlistmaker2.search.data.SearchRepository
import com.example.playlistmaker2.search.domain.model.Track
import com.example.playlistmaker2.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

open class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {

    override fun searchMusic(expression: String): Flow<Pair<List<Track>?, String?>> {
        return  repository.searchMusic(expression).map {  result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }

    }
}