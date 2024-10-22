package com.example.playlistmaker2.media.data

import com.example.playlistmaker2.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavouritesMediaRepository {

        suspend fun addFavouritesTrack(track : Track)

        suspend fun deleteFavouritesTrack(track: Track)

        suspend fun getAllFavouritesTrack(): Flow<List<Track>>

        suspend fun isFavoriteCheck(trackId: String):Boolean


}