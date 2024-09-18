package com.example.playlistmaker2.util

import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.example.playlistmaker2.search.data.HistoryRepository
import com.example.playlistmaker2.search.data.SearchRepository
import com.example.playlistmaker2.search.data.impl.HistoryRepositoryImpl
import com.example.playlistmaker2.search.data.impl.SearchRepositoryImpl
import com.example.playlistmaker2.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker2.search.domain.HistoryInteractor
import com.example.playlistmaker2.search.domain.SearchInteractor
import com.example.playlistmaker2.search.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker2.search.domain.impl.SearchInteractorImpl
import com.google.gson.Gson

object CreatorSearch {

    private fun getMusicRepository(connectivityManager: ConnectivityManager): SearchRepository {
        return SearchRepositoryImpl(RetrofitNetworkClient(connectivityManager))
    }

    fun provideMusicInteractor(connectivityManager: ConnectivityManager): SearchInteractor {
        return SearchInteractorImpl(getMusicRepository(connectivityManager))
    }

    private fun getHistoryRepository(sharedPrefs: SharedPreferences, gson: Gson) : HistoryRepository {
        return HistoryRepositoryImpl(sharedPrefs, gson)
    }

    fun provideHistoryInteractor(sharedPrefs: SharedPreferences, gson: Gson): HistoryInteractor {
        return HistoryInteractorImpl(getHistoryRepository(sharedPrefs, gson))
    }
}