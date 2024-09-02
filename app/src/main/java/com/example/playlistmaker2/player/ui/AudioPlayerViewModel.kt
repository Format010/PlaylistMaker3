package com.example.playlistmaker2.player.ui

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker2.player.domain.AudioPlayerInteractor
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.example.playlistmaker2.App
import com.example.playlistmaker2.CLICK_DEBOUNCE_DELAY
import com.example.playlistmaker2.DELAY

class AudioPlayerViewModel(
    private val audioPlayerInteractor: AudioPlayerInteractor,
    private val url: String?
): ViewModel() {

    companion object {
        fun getViewModelFactory(url: String?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val interactor = (this[APPLICATION_KEY] as App).providePlayerInteractor()
                AudioPlayerViewModel(interactor, url)
            }
        }
    }

    private lateinit var runnable: Runnable
    private var isClickAllowed = true

    private val _isPlaying = MutableLiveData<Boolean>(false)
    val isPlaying: LiveData<Boolean> get() = _isPlaying

    private val _currentPosition = MutableLiveData<Int>(0)
    val currentPosition: LiveData<Int> get() = _currentPosition

    private val handler = Handler(Looper.getMainLooper())
    private val updatePositionRunnable = object : Runnable {
        override fun run() {
            if (isPlaying.value == true) {
                _currentPosition.value = position()
                handler.postDelayed(this, CLICK_DEBOUNCE_DELAY)
            }
        }
    }

    init {
        setOnCompletionListener {
            _isPlaying.value = false
            _currentPosition.value = 0
        }
    }

    fun preparedPlayer() {
        if (url != null) { audioPlayerInteractor.initializePlayer(url)
        }
    }

    fun position(): Int{
       return audioPlayerInteractor.position() ?: 0
    }

    fun play(){

        audioPlayerInteractor.play()
        seekTo()
        _isPlaying.value = true
    }

    fun pause(){
        audioPlayerInteractor.pause()
        _isPlaying.value = false
    }

    fun stop(){
        audioPlayerInteractor.stop()
        _isPlaying.value = false
        _currentPosition.value = 0
    }

    fun release(){
        audioPlayerInteractor.release()
        _isPlaying.value = false
    }

    fun setOnCompletionListener(listener: MediaPlayer.OnCompletionListener){
        audioPlayerInteractor.setOnCompletionListener(listener)
    }

    fun formatTime(formatTime: String):String{
        return audioPlayerInteractor.formatTime(formatTime)

    }

    fun seekTo(){
        audioPlayerInteractor.seekTo(currentPosition.value ?: 0)
        startUpdatingPosition()
    }

    override fun onCleared() {
        audioPlayerInteractor.release()

        super.onCleared()
    }

    private fun startUpdatingPosition() {
        handler.post(updatePositionRunnable)
    }

    fun startTimer(textView: TextView) {
        runnable = object : Runnable {
            override fun run() {
                textView.text = formatTime(currentPosition.value.toString())
                handler.postDelayed(this, DELAY)
            }
        }
        handler.postDelayed(runnable, DELAY)
    }

    fun stopTimer(textView: TextView) {
        textView.text = formatTime(currentPosition.value.toString())
        handler.removeCallbacks(runnable)
    }

    fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

}