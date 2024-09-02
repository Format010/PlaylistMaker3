package com.example.playlistmaker2.util

import android.content.Context
import com.example.playlistmaker2.search.data.HistoryRepository
import com.example.playlistmaker2.search.data.SearchRepository
import com.example.playlistmaker2.search.data.impl.HistoryRepositoryImpl
import com.example.playlistmaker2.search.data.impl.SearchRepositoryImpl
import com.example.playlistmaker2.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker2.search.domain.HistoryInteractor
import com.example.playlistmaker2.search.domain.SearchInteractor
import com.example.playlistmaker2.search.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker2.search.domain.impl.SearchInteractorImpl

object CreatorSearch {

    private fun getMusicRepository(context: Context): SearchRepository {
        return SearchRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideMusicInteractor(context: Context): SearchInteractor {
        return SearchInteractorImpl(getMusicRepository(context))
    }

    private fun getHistoryRepository() : HistoryRepository {
        return HistoryRepositoryImpl()
    }

    fun provideHistoryInteractor(): HistoryInteractor {
        return HistoryInteractorImpl(getHistoryRepository())
    }
}