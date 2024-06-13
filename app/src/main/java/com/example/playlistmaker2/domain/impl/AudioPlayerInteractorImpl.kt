package com.example.playlistmaker2.domain.impl

import android.media.MediaPlayer
import android.widget.TextView
import com.example.playlistmaker2.domain.api.AudioPlayerInteractor
import com.example.playlistmaker2.domain.api.AudioPlayerRepository

class AudioPlayerInteractorImpl(private val audioPlayerRepository: AudioPlayerRepository) : AudioPlayerInteractor {

    override fun initializePlayer(
        resourceId: String,
        onCompletionListener: MediaPlayer.OnCompletionListener?
    ) {
        audioPlayerRepository.initializePlayer(resourceId,onCompletionListener)
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

    override fun isPlaying(): Boolean {
        return audioPlayerRepository.isPlaying()
    }

    override fun setOnCompletionListener(listener: MediaPlayer.OnCompletionListener) {
        audioPlayerRepository.setOnCompletionListener(listener)
    }

    override fun startTimer(textView: TextView) {
        audioPlayerRepository.startTimer(textView)
    }

    override fun stopTimer(textView: TextView) {
        audioPlayerRepository.stopTimer(textView)
    }


}