package com.example.playlistmaker2.player.domain.impl

import android.media.MediaPlayer
import android.widget.TextView
import com.example.playlistmaker2.player.domain.AudioPlayerInteractor
import com.example.playlistmaker2.player.data.AudioPlayerRepository

class AudioPlayerInteractorImpl(private val audioPlayerRepository: AudioPlayerRepository) :
    AudioPlayerInteractor {

    override fun initializePlayer(
        resourceId: String,
    ) {
        audioPlayerRepository.initializePlayer(resourceId,)
    }

    override fun play() {
        audioPlayerRepository.play()
    }

    override fun pause() {
        audioPlayerRepository.pause()
    }

    override fun stop() {
        audioPlayerRepository.stop()
    }

    override fun release() {
        audioPlayerRepository.release()
    }

    override fun setOnCompletionListener(listener: MediaPlayer.OnCompletionListener) {
        audioPlayerRepository.setOnCompletionListener(listener)
    }

    override fun startTimer(textView: TextView,) {
        audioPlayerRepository.startTimer(textView)
    }

    override fun stopTimer(textView: TextView) {
        audioPlayerRepository.stopTimer(textView)
    }

    override fun formatTime(formatTime: String): String {
        return audioPlayerRepository.formatTime(formatTime)
    }

    override fun position(): Int? {
        return audioPlayerRepository.position()
    }

    override fun seekTo(curentPosition: Int){
        audioPlayerRepository.seekTo(curentPosition)
    }

}