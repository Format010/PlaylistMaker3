package com.example.playlistmaker2.creator

import com.example.playlistmaker2.data.network.MusicRepositoryImpl
import com.example.playlistmaker2.data.network.RetrofitNetworkClient
import com.example.playlistmaker2.domain.api.MusicInteractor
import com.example.playlistmaker2.domain.api.MusicRepository
import com.example.playlistmaker2.domain.impl.MusicInteractorImpl

object Creator {
    private fun getMusicRepository(): MusicRepository {
        return MusicRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideMusicInteractor(): MusicInteractor {
        return MusicInteractorImpl(getMusicRepository())
    }
}