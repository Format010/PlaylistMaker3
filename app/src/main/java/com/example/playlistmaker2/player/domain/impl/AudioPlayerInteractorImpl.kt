package com.example.playlistmaker2.player.domain.impl

import com.example.playlistmaker2.player.data.AudioPlayerRepository
import com.example.playlistmaker2.player.domain.AudioPlayerInteractor

class AudioPlayerInteractorImpl(private val audioPlayerRepository: AudioPlayerRepository) :
    AudioPlayerInteractor {

    override fun preparedPlayer(
        url: String
    ) {
        audioPlayerRepository.preparedPlayer(url)
    }

    override fun play() {
        audioPlayerRepository.play()
    }

    override fun pause() {
        audioPlayerRepository.pause()
    }

    override fun release() {
        audioPlayerRepository.release()
    }

    override fun onCompletionListener(): Boolean {
        return audioPlayerRepository.onCompletionListener()
    }

    override fun isPlaying(): Boolean? {
        return audioPlayerRepository.isPlaying()
    }

    override fun position(): Int? {
        return audioPlayerRepository.position()
    }

    override fun seekTo(curentPosition: Int){
        audioPlayerRepository.seekTo(curentPosition)
    }

}