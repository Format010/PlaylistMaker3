package com.example.playlistmaker2.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker2.CLICK_DEBOUNCE_DELAY
import com.example.playlistmaker2.player.domain.AudioPlayerInteractor
import com.example.playlistmaker2.player.domain.models.AudioPlayerStateStatus
import com.example.playlistmaker2.player.domain.models.AudioPlayerUiState

class AudioPlayerViewModel(
    private val playerInteractor: AudioPlayerInteractor,
    private val url: String?
) : ViewModel() {

    private var _playerUiState = MutableLiveData(AudioPlayerUiState())
    val getPlayerState: LiveData<AudioPlayerUiState> get() = _playerUiState

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private val updatePositionRunnable = object : Runnable {
        override fun run() {
            if (playerInteractor.onCompletionListener()) {
                updateUiState(onCompletionListener = playerInteractor.onCompletionListener())
            }

            if (playerInteractor.isPlaying() == true) {
                updateUiState(currentPosition = playerInteractor.position())
                handler.postDelayed(this, 1000)
            }
        }
    }

    private var currentState = _playerUiState.value ?: AudioPlayerUiState()

    private fun updateUiState(
        currentPosition: Int? = currentState.currentPosition,
        isPlaying: Boolean? = currentState.isPlaying,
        audioPlayerStateStatus: AudioPlayerStateStatus? = currentState.audioPlayerStateStatus,
        onCompletionListener: Boolean? = currentState.onCompletionListener
    ) {
        val updatedState = currentState.copy(
            currentPosition = currentPosition ?: currentState.currentPosition,
            isPlaying = isPlaying ?: currentState.isPlaying,
            audioPlayerStateStatus = audioPlayerStateStatus ?: currentState.audioPlayerStateStatus,
            onCompletionListener = onCompletionListener ?: currentState.onCompletionListener
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
        seekTo()
        updateUiState(
            isPlaying = true,
            audioPlayerStateStatus = AudioPlayerStateStatus.PLAYING,
        )
        playerInteractor.play()
    }

    fun pause() {
        playerInteractor.pause()
        updateUiState(
            isPlaying = false,
            audioPlayerStateStatus = AudioPlayerStateStatus.PAUSED,
        )
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
        playerInteractor.release()
        updateUiState(
            isPlaying = false,
            audioPlayerStateStatus = AudioPlayerStateStatus.DEFAULT,
            currentPosition = 0,
            onCompletionListener = false
        )
        handler.removeCallbacks(updatePositionRunnable)
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
        handler.post(updatePositionRunnable)
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

}