package com.example.playlistmaker2.player.domain

interface AudioPlayerInteractor {

    fun preparedPlayer(url: String)

    fun play()

    fun pause()

    fun release()

    fun onCompletionListener():Boolean

    fun isPlaying(): Boolean?

    fun position(): Int?

    fun seekTo(curentPosition: Int)
}