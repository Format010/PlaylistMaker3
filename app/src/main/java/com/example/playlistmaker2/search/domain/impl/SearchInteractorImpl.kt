package com.example.playlistmaker2.search.domain.impl

import com.example.playlistmaker2.search.domain.SearchInteractor
import com.example.playlistmaker2.search.data.SearchRepository
import com.example.playlistmaker2.util.Resource
import java.util.concurrent.Executors

open class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchMusic(expression: String, consumer: SearchInteractor.MusicConsumer) {
        executor.execute {
            when(val resource = repository.searchMusic(expression)) {
                is Resource.Success -> consumer.consume(resource.data, null)
                is Resource.Error -> { consumer.consume(null, resource.message)}
            }
        }
    }
}