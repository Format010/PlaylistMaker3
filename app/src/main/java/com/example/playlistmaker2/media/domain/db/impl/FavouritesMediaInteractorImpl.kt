package com.example.playlistmaker2.media.domain.db.impl

import com.example.playlistmaker2.media.data.FavouritesMediaRepository
import com.example.playlistmaker2.media.domain.db.FavouritesMediaInteractor
import com.example.playlistmaker2.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class FavouritesMediaInteractorImpl (
    private val favouritesMediaRepository: FavouritesMediaRepository
): FavouritesMediaInteractor {

    override suspend fun addFavouritesTrack(track: Track) {
        favouritesMediaRepository.addFavouritesTrack(track)
    }

    override suspend fun deleteFavouritesTrack(track: Track) {
        favouritesMediaRepository.deleteFavouritesTrack(track)
    }

    override suspend fun getAllFavouritesTrack(): Flow<List<Track>> {
        return favouritesMediaRepository.getAllFavouritesTrack()
    }

    override suspend fun isFavoriteCheck(trackId: String): Boolean {
        return favouritesMediaRepository.isFavoriteCheck(trackId)
    }

}