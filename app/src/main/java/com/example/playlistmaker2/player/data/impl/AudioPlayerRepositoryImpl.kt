package com.example.playlistmaker2.player.data.impl

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.example.playlistmaker2.DELAY
import com.example.playlistmaker2.player.data.AudioPlayerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerRepositoryImpl: AudioPlayerRepository {

    private var mediaPlayer: MediaPlayer? = null
    private val playerScope = CoroutineScope(Dispatchers.Main + Job())
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable
    private lateinit var onCompletionListener : OnCompletionListener

    override fun initializePlayer(
        resourceId: String,
    ) {
        playerScope.launch {
            withContext(Dispatchers.IO) {
                mediaPlayer = MediaPlayer()
                    .apply {
                        setAudioAttributes(
                            AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA).build()
                        )
                        setDataSource(resourceId)
                        prepare()
                    }
            }
            mediaPlayer?.setOnCompletionListener {
                onCompletionListener.onCompletion(mediaPlayer)
            }
        }
    }

    override fun play() {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
            }
        }
    }

    override fun pause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
            }
        }
    }

    override fun stop() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
                playerScope.launch {
                    withContext(Dispatchers.IO) {
                        it.prepare()
                    }
                }
            }
        }
    }

    override fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    //слушатель
    override fun setOnCompletionListener(listener: OnCompletionListener) {
       onCompletionListener = listener
    }

    override fun startTimer(textView: TextView) {
        runnable = object : Runnable {
            override fun run() {
                textView.text = formatTime(mediaPlayer?.currentPosition.toString())
                handler.postDelayed(this, DELAY)
            }
        }
        handler.postDelayed(runnable, DELAY)
    }

    override fun stopTimer(textView: TextView) {
        textView.text = formatTime("0")
        handler.removeCallbacks(runnable)
    }

    override fun formatTime(formatTime: String): String {
            return SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(formatTime.toLong())
    }

    override fun position(): Int? {
        return mediaPlayer?.currentPosition
    }

    override fun seekTo(curentPosition: Int) {
        mediaPlayer?.seekTo(curentPosition)
    }

}