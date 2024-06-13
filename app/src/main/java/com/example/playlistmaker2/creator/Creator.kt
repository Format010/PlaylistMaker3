package com.example.playlistmaker2.creator

import com.example.playlistmaker2.data.MusicRepositoryImpl
import com.example.playlistmaker2.data.network.RetrofitNetworkClient
import com.example.playlistmaker2.data.platform.AudioPlayerRepositoryImpl
import com.example.playlistmaker2.domain.api.AudioPlayerInteractor
import com.example.playlistmaker2.domain.api.AudioPlayerRepository
import com.example.playlistmaker2.domain.api.MusicInteractor
import com.example.playlistmaker2.domain.api.MusicRepository
import com.example.playlistmaker2.domain.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker2.domain.impl.MusicInteractorImpl

object Creator {
    private fun getMusicRepository(): MusicRepository {
        return MusicRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideMusicInteractor(): MusicInteractor {
        return MusicInteractorImpl(getMusicRepository())
    }

    private fun provideAudioPlayerRepository(): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl()
    }

    fun providePlayerInteractor(): AudioPlayerInteractor {
        val playerRepository = provideAudioPlayerRepository()
        return AudioPlayerInteractorImpl(playerRepository)
    }
}