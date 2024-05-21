package com.example.playlistmaker2.domain.impl

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.example.playlistmaker2.data.MediaPlayerInt
import com.example.playlistmaker2.data.dto.TimeFormatting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MediaPlayerImpl(private val context: Context): MediaPlayerInt {

    companion object {
        private const val DELAY = 500L
    }

    private var mediaPlayer: MediaPlayer? = null
    private val playerScope = CoroutineScope(Dispatchers.Main + Job())
    lateinit var runnable: Runnable
    private val handler = Handler(Looper.getMainLooper())

    override fun inicializePlayer(
        resourceId: String,
        onCompletionListener: MediaPlayer.OnCompletionListener?
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
            onCompletionListener?.let { setOnCompletionListener(it) }
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

    //проверка играит ли плеер
    override fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    //слушатель
    override fun setOnCompletionListener(listener: MediaPlayer.OnCompletionListener) {
        mediaPlayer?.setOnCompletionListener(listener)
    }

    //Таймер
    override fun startTimer(textView: TextView) {
        runnable = object : Runnable {
            override fun run() {
                textView.text =
                    TimeFormatting().simpleDataFormat(mediaPlayer?.currentPosition.toString())
                handler.postDelayed(this, DELAY)
            }
        }
            handler.postDelayed(runnable, DELAY)
    }

    override fun stopTimer(textView: TextView) {
        textView.text = TimeFormatting().simpleDataFormat("0")
        handler.removeCallbacks(runnable)
    }
}