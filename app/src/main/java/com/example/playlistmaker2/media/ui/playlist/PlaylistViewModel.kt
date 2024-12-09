package com.example.playlistmaker2.media.ui.playlist


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker2.R
import com.example.playlistmaker2.media.domain.PlaylistInteractor
import com.example.playlistmaker2.media.domain.model.Playlist
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch


class PlaylistViewModel(private val context: Context,
                        private val interactor: PlaylistInteractor
): ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistState>()
    val stateLive: LiveData<PlaylistState> = stateLiveData

    fun getPlaylists(){
        viewModelScope.launch {
            interactor.getAllPlaylists()
                .onStart { render(PlaylistState.Loading) }
                .collect{playlist->
                    showData(playlist)
                }
        }
    }

    private fun showData(playlists: List<Playlist>){
        if (playlists.isEmpty()){
            render(PlaylistState.Empty(context.getString(R.string.empty_playlist)))
        }else{
            render(PlaylistState.Content(playlists))

        }
    }

    private fun render(state: PlaylistState){
        stateLiveData.postValue(state)
    }

}