package com.example.playlistmaker2.media.ui.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker2.media.domain.PlaylistInteractor
import com.example.playlistmaker2.media.domain.model.Playlist
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(val interactor: PlaylistInteractor): ViewModel() {

    private val playlistsCoverPathList = mutableListOf<String>()

    fun addNewPlaylistToDb(playlist: Playlist) {
        viewModelScope.launch {
            interactor.addNewPlaylist(playlist)
        }
    }

    fun writeFileName(filePath: String) {
        if (!playlistsCoverPathList.contains(filePath)) {
            playlistsCoverPathList.add(filePath)
        }
    }

    fun getImagePath(): String? {
        return playlistsCoverPathList.lastOrNull()
    }

}