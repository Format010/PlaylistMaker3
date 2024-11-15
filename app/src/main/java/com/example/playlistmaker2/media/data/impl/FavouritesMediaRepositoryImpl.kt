package com.example.playlistmaker2.media.data.impl

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.playlistmaker2.media.domain.FavouritesMediaRepository
import com.example.playlistmaker2.media.data.converter.TrackConverter
import com.example.playlistmaker2.media.data.db.AppDatabase
import com.example.playlistmaker2.media.data.db.entity.FavouritesMediaEntity
import com.example.playlistmaker2.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavouritesMediaRepositoryImpl(
    private val database: AppDatabase,
    private val converter: TrackConverter,
): FavouritesMediaRepository {

    override suspend fun addFavouritesTrack(track: Track) {
        val trackFavoriteMedia = converter.mapTrackToEntity(track)
        database.favouritesMediaDao().insertFavouritesMediaEntity(trackFavoriteMedia)
    }

    override suspend fun deleteFavouritesTrack(track: Track) {
        val trackFavoriteMedia = converter.mapTrackToEntity(track)
        database.favouritesMediaDao().deleteFavouritesMediaEntity(trackFavoriteMedia)
    }

    override suspend fun getAllFavouritesTrack(): Flow<List<Track>> {
        return database.favouritesMediaDao().getFavouritesMediaEntity().map {convertFromTrackEntity(it)}
    }

    override suspend fun isFavoriteCheck(trackId: String): Boolean {
        return database.favouritesMediaDao().searchMoviesById().contains(trackId)
    }

    private fun convertFromTrackEntity(movies: List<FavouritesMediaEntity>): List<Track> {
        return movies.map { movie -> converter.mapEntityToTrack(movie) }
    }

}