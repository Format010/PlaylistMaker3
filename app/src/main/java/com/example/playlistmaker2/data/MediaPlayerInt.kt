package com.example.playlistmaker2.data

import android.media.MediaPlayer
import android.widget.TextView
import com.example.playlistmaker2.domain.models.MusicPlayerState

interface MediaPlayerInt {
    fun inicializePlayer(resourceId: String, onCompletionListener: MediaPlayer.OnCompletionListener?)

    fun play()

    fun pause()

    fun stop()

    fun release()

    fun isPlaying(): Boolean

    fun setOnCompletionListener(listener: MediaPlayer.OnCompletionListener)

    fun startTimer(textView: TextView)

    fun stopTimer(textView: TextView)

}

