package com.example.playlistmaker2.domain.api

import android.media.MediaPlayer
import android.widget.TextView


interface AudioPlayerInteractor {

    fun initializePlayer(resourceId: String, onCompletionListener: MediaPlayer.OnCompletionListener?)

    fun play()

    fun pause()

    fun stop()

    fun release()

    fun isPlaying(): Boolean

    fun setOnCompletionListener(listener: MediaPlayer.OnCompletionListener)

    fun startTimer(textView: TextView)

    fun stopTimer(textView: TextView)

}