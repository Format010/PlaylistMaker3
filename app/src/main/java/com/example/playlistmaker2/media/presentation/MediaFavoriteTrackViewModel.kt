package com.example.playlistmaker2.media.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker2.media.domain.db.FavouritesMediaInteractor
import com.example.playlistmaker2.media.ui.FauvoriteTrackState
import com.example.playlistmaker2.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MediaFavoriteTrackViewModel(
    private val favouritesMediaInteractor: FavouritesMediaInteractor
): ViewModel() {

    private var _favouriteTrackUiState = MutableLiveData<FauvoriteTrackState>()
    val favouriteTrackUiState: LiveData<FauvoriteTrackState> get() = _favouriteTrackUiState

    fun loadDate() {
        render(FauvoriteTrackState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            favouritesMediaInteractor.getAllFavouritesTrack().collect{ tracks -> showData(tracks)}
        }
    }

    private fun showData(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            render(FauvoriteTrackState.Empty)
        } else {
            render(FauvoriteTrackState.Content(tracks))
        }
    }

    private fun render(state: FauvoriteTrackState) {
        _favouriteTrackUiState.postValue(state)
    }

}