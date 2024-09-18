package com.example.playlistmaker2.player.ui

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker2.CLICK_DEBOUNCE_DELAY
import com.example.playlistmaker2.player.domain.models.AudioPlayerStateStatus
import com.example.playlistmaker2.player.domain.models.AudioPlayerUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AudioPlayerViewModel(

    private val url: String?
) : ViewModel() {

    companion object {
        fun getViewModelFactory(url: String?): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AudioPlayerViewModel(
                        url
                    ) as T
                }
            }
    }

    private var _playerUiState = MutableLiveData(AudioPlayerUiState())
    val getPlayerState: LiveData<AudioPlayerUiState> get() = _playerUiState

    private val mediaPlayer: MediaPlayer by lazy {
        MediaPlayer()
    }

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())
    private val playerScope = CoroutineScope(Dispatchers.Main + Job())

    private val updatePositionRunnable = object : Runnable {
        override fun run() {
            if (mediaPlayer.isPlaying) {
                updateUiState(currentPosition = mediaPlayer.currentPosition)
                handler.postDelayed(this, 1000)
            }
        }
    }

    init {
        mediaPlayer.setOnCompletionListener {
            updateUiState(
                isPlaying = false,
                currentPosition = 0,
                audioPlayerStateStatus = AudioPlayerStateStatus.STATE_PREPARED
            )
            handler.removeCallbacks(updatePositionRunnable)
        }
    }


    private var currentState = _playerUiState.value ?: AudioPlayerUiState()

    private fun updateUiState(
        currentPosition: Int? = currentState.currentPosition,
        isPlaying: Boolean? = currentState.isPlaying,
        audioPlayerStateStatus: AudioPlayerStateStatus? = currentState.audioPlayerStateStatus
    ) {
        val updatedState = currentState.copy(
            currentPosition = currentPosition ?: currentState.currentPosition,
            isPlaying = isPlaying ?: currentState.isPlaying,
            audioPlayerStateStatus = audioPlayerStateStatus ?: currentState.audioPlayerStateStatus
        )
        _playerUiState.postValue(updatedState)
        currentState = updatedState
    }

    fun preparedPlayer() {
        if (url != null) {
            playerScope.launch {
                withContext(Dispatchers.IO) {
                    mediaPlayer
                        .apply {
                            setAudioAttributes(
                                AudioAttributes.Builder()
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                    .setUsage(AudioAttributes.USAGE_MEDIA).build()
                            )
                            setDataSource(url)
                            prepare()
                            updateUiState(audioPlayerStateStatus = AudioPlayerStateStatus.STATE_PREPARED)
                        }
                }
            }
        }
    }

    fun play() {
        seekTo()
        updateUiState(
            isPlaying = true,
            audioPlayerStateStatus = AudioPlayerStateStatus.STATE_PLAYING,
        )

        mediaPlayer.let {
            if (!it.isPlaying) {
                it.start()
            }
        }

    }

    fun pause() {
        mediaPlayer.let {
            if (it.isPlaying) {
                it.pause()
            }
        }
        updateUiState(
            isPlaying = false,
            audioPlayerStateStatus = AudioPlayerStateStatus.STATE_PAUSED,
        )
    }

    private fun release() {
        mediaPlayer.release()
        updateUiState(
            isPlaying = false,
            audioPlayerStateStatus = AudioPlayerStateStatus.STATE_DEFAULT,
            currentPosition = 0
        )
        handler.removeCallbacks(updatePositionRunnable)
    }

    private fun seekTo() {
        mediaPlayer.seekTo(_playerUiState.value?.currentPosition ?: 0)
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