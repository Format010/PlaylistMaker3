package com.example.playlistmaker2.player.data.impl

import android.media.AudioAttributes
import android.media.MediaPlayer
import com.example.playlistmaker2.player.data.AudioPlayerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AudioPlayerRepositoryImpl: AudioPlayerRepository {

    private var mediaPlayer: MediaPlayer? = null
    private val playerScope = CoroutineScope(Dispatchers.Main + Job())
    private var endSong = false

    override fun preparedPlayer(url: String) {
        playerScope.launch {
            withContext(Dispatchers.IO) {
                mediaPlayer = MediaPlayer()
                    .apply {
                        setAudioAttributes(
                            AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA).build()
                        )
                        setDataSource(url)
                        prepare()
                    }
            }
            mediaPlayer?.setOnCompletionListener {

                endSong = true
            }
        }
    }

    override fun play() {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
            }
        }
        endSong = false
    }

    override fun pause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
            }
        }
    }

    override fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onCompletionListener(): Boolean {
        return endSong
    }

    override fun isPlaying(): Boolean? {
    return mediaPlayer?.isPlaying
    }

    override fun position(): Int? {
        return mediaPlayer?.currentPosition
    }

    override fun seekTo(curentPosition: Int) {
        mediaPlayer?.seekTo(curentPosition)
    }


}