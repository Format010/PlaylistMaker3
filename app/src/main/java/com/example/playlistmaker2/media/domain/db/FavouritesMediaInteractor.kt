package com.example.playlistmaker2.media.domain.db

import com.example.playlistmaker2.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavouritesMediaInteractor {

    suspend fun addFavouritesTrack(track : Track)

    suspend fun deleteFavouritesTrack(track: Track)

    suspend fun getAllFavouritesTrack(): Flow<List<Track>>

    suspend fun isFavoriteCheck(trackId: String):Boolean

}