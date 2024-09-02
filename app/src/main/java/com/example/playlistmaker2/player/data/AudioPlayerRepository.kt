package com.example.playlistmaker2.player.data

import android.media.MediaPlayer
import android.widget.TextView

interface AudioPlayerRepository {
    fun initializePlayer(resourceId: String,)

    fun play()

    fun pause()

    fun stop()

    fun release()

    fun setOnCompletionListener(listener: MediaPlayer.OnCompletionListener)

    fun startTimer(textView: TextView)

    fun stopTimer(textView: TextView)

    fun formatTime(formatTime: String) : String

    fun position(): Int?

    fun seekTo(curentPosition: Int)

}