package com.example.playlistmaker2.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker2.CLICK_DEBOUNCE_DELAY
import com.example.playlistmaker2.R
import com.example.playlistmaker2.TIMER_MUSIC_DELAY
import com.example.playlistmaker2.media.domain.FavouritesMediaInteractor
import com.example.playlistmaker2.media.domain.PlaylistInteractor
import com.example.playlistmaker2.media.domain.model.Playlist
import com.example.playlistmaker2.media.ui.playlist.PlaylistState
import com.example.playlistmaker2.player.domain.AudioPlayerInteractor
import com.example.playlistmaker2.player.domain.models.AudioPlayerStateStatus
import com.example.playlistmaker2.player.domain.models.AudioPlayerUiState
import com.example.playlistmaker2.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val playerInteractor: AudioPlayerInteractor,
    private val favouritesMediaInteractor: FavouritesMediaInteractor,
    private val playlistInteractor: PlaylistInteractor,
    private val url: String?,
    private val idTrack: String?
) : ViewModel() {

    private var _playerUiState = MutableLiveData(AudioPlayerUiState())
    val getPlayerState: LiveData<AudioPlayerUiState> get() = _playerUiState
    private var isClickAllowed = true
    private var timerJob: Job? = null
    private var currentState = _playerUiState.value ?: AudioPlayerUiState()

    init {
        isFavoriteTrack()

    }

    private fun updateUiState(
        currentPosition: Int? = currentState.currentPosition,
        isPlaying: Boolean? = currentState.isPlaying,
        audioPlayerStateStatus: AudioPlayerStateStatus? = currentState.audioPlayerStateStatus,
        onCompletionListener: Boolean? = currentState.onCompletionListener,
        favouritesTrack: Boolean? = currentState.favouritesTrack,
        playlistState: BottomSheetState = currentState.playlistState,
        insertTrackState: BottomSheetState = BottomSheetState.EmptyTrack
    ) {
        val updatedState = currentState.copy(
            currentPosition = currentPosition ?: currentState.currentPosition,
            isPlaying = isPlaying ?: currentState.isPlaying,
            audioPlayerStateStatus = audioPlayerStateStatus ?: currentState.audioPlayerStateStatus,
            onCompletionListener = onCompletionListener ?: currentState.onCompletionListener,
            favouritesTrack = favouritesTrack ?: currentState.favouritesTrack,
            playlistState = playlistState,
            insertTrackState = insertTrackState
        )
        _playerUiState.postValue(updatedState)
        currentState = updatedState
    }

    fun preparedPlayer() {
        if (url != null) {
            playerInteractor.preparedPlayer(url)
            updateUiState(audioPlayerStateStatus = AudioPlayerStateStatus.PREPARED)
        }
    }

    fun play() {
        updateUiState(
            isPlaying = true,
            audioPlayerStateStatus = AudioPlayerStateStatus.PLAYING,
        )
        seekTo()
        playerInteractor.play()
    }

    fun pause() {
        updateUiState(
            isPlaying = false,
            audioPlayerStateStatus = AudioPlayerStateStatus.PAUSED,
        )
        playerInteractor.pause()
    }

    fun endSong() {
        updateUiState(
            isPlaying = false,
            currentPosition = 0,
            audioPlayerStateStatus = AudioPlayerStateStatus.PREPARED,
            onCompletionListener = false
        )
    }

    private fun release() {
        updateUiState(
            isPlaying = false,
            audioPlayerStateStatus = AudioPlayerStateStatus.DEFAULT,
            currentPosition = 0,
            onCompletionListener = false
        )
        playerInteractor.release()
    }

    private fun seekTo() {
        playerInteractor.seekTo(_playerUiState.value?.currentPosition ?: 0)
        startUpdatingPosition()
    }

    override fun onCleared() {
        super.onCleared()
        release()
    }

    private fun startUpdatingPosition() {
        timerJob = viewModelScope.launch {
            while(currentState.isPlaying) {
                updateUiState(currentPosition = playerInteractor.position())
                if (playerInteractor.onCompletionListener()) {
                    updateUiState(onCompletionListener = playerInteractor.onCompletionListener())
                }
                delay(TIMER_MUSIC_DELAY)
            }
        }
    }

    private fun isFavoriteTrack() {
        viewModelScope.launch(Dispatchers.IO) {
            if (favouritesMediaInteractor.isFavoriteCheck(idTrack!!)) {
                updateUiState(favouritesTrack = true)
            } else {
                updateUiState(favouritesTrack = false)
            }
        }
    }

    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            if (currentState.favouritesTrack){
                favouritesMediaInteractor.deleteFavouritesTrack(track)
                updateUiState(favouritesTrack = false)
            }else {
                favouritesMediaInteractor.addFavouritesTrack(track)
                updateUiState(favouritesTrack = true)
            }
        }
    }

    fun getPlaylist() {
        updateUiState(playlistState = BottomSheetState.Loading)
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists()
                .collect { playlist ->
                    showData(playlist)
                }
        }
    }

    private fun showData(playlist: List<Playlist>) {
        if (playlist.isEmpty()) {
            updateUiState(playlistState = BottomSheetState.Empty)
        } else {
            updateUiState(playlistState = BottomSheetState.Content(playlist))
        }
    }

    fun addTrackToPLaylist(track: Track, playlist: Playlist){
        viewModelScope.launch {
            if (playlistInteractor.checkTrackInPlaylist(track.trackId ?: "", playlist)){
                updateUiState(insertTrackState = BottomSheetState.IsertTrack(playlist.title, false))

            }else {
                updateUiState(insertTrackState = BottomSheetState.IsertTrack(playlist.title, true))
                playlistInteractor.addTrackToPlaylist(track, playlist)


            }
        }
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

}