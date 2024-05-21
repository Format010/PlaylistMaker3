package com.example.playlistmaker2.domain.impl

import com.example.playlistmaker2.domain.api.MusicInteractor
import com.example.playlistmaker2.domain.api.MusicRepository
import java.util.concurrent.Executors

class MusicInteractorImpl(private val repository: MusicRepository) : MusicInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchMusic(expression: String, consumer: MusicInteractor.MusicConsumer) {
        executor.execute {
            consumer.consume(repository.searchMusic(expression))
        }
    }
}