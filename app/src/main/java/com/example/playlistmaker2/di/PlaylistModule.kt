package com.example.playlistmaker2.di

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.playlistmaker2.media.data.converter.PlaylistConverter
import com.example.playlistmaker2.media.data.db.AppDatabase
import com.example.playlistmaker2.media.data.impl.PlaylistRepositoryImpl
import com.example.playlistmaker2.media.domain.PlaylistInteractor
import com.example.playlistmaker2.media.domain.PlaylistRepository
import com.example.playlistmaker2.media.domain.impl.PlaylistInteractorImpl
import com.example.playlistmaker2.media.ui.playlist.CreatePlaylistViewModel
import com.example.playlistmaker2.media.ui.playlist.PlaylistViewModel
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val playlistModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "databasePlaylist.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    factory { PlaylistConverter() }
    factory { Gson() }


    single<PlaylistRepository> {
        PlaylistRepositoryImpl(
            playlistDao = get<AppDatabase>().playlistDao(),
            playlistConverter = get(),
            gson = get(),
            trackPlaylistDao = get<AppDatabase>().trackPlaylistDao()

        )
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }

    viewModel {
        CreatePlaylistViewModel(interactor = get())
    }
    viewModel {
        PlaylistViewModel(context = androidContext(), interactor = get())
    }

}