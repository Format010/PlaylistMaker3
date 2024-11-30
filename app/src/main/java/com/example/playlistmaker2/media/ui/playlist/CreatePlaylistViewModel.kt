package com.example.playlistmaker2.media.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker2.media.domain.PlaylistInteractor
import com.example.playlistmaker2.media.domain.model.Playlist
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(val interactor: PlaylistInteractor): ViewModel() {

    private val playlistsLiveData = MutableLiveData<Playlist>()
    val playlistLive: LiveData<Playlist> = playlistsLiveData

    fun addNewPlaylistToDb(playlist: Playlist) {
        viewModelScope.launch {
            interactor.addNewPlaylist(playlist)
        }
    }

    fun getDataPlaylist(plalistId: Int) {
        viewModelScope.launch {
            playlistsLiveData.value = interactor.getPLaylistById(plalistId)
        }
    }

    fun updatePlaylist(playlist: Playlist){
        viewModelScope.launch {
            interactor.update(playlist)
        }
    }
}