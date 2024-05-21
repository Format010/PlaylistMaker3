package com.example.playlistmaker2

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import layout.AUDIO_PLAYER_DATA
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayer() : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 500L
    }

    private var playerState = STATE_DEFAULT
    private var play: ImageView? = null
    private var url: String? = null
    private var mediaPlayer = MediaPlayer()
    private var timer: TextView? = null
    private val handler = Handler(Looper.getMainLooper())
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val track =
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) {
                intent?.extras?.getParcelable(AUDIO_PLAYER_DATA, Track::class.java)
            } else {
                intent?.extras?.getParcelable(AUDIO_PLAYER_DATA)
            }

        play = findViewById(R.id.play)
        timer = findViewById(R.id.timing)
        val back = findViewById<Button>(R.id.back_ap)
        val artwork = findViewById<ImageView>(R.id.artwork)
        val trackName = findViewById<TextView>(R.id.track_name)
        val artistName = findViewById<TextView>(R.id.artist_name)
        val addPlayList = findViewById<ImageView>(R.id.check)
        val addFavorite = findViewById<ImageView>(R.id.favorite)
        val trackTimeMills = findViewById<TextView>(R.id.track_time_mills)
        val collectionName = findViewById<TextView>(R.id.collection_name)
        val collectionNameArea = findViewById<TextView>(R.id.collection_name_area)
        val releaseDate = findViewById<TextView>(R.id.release_date)
        val primaryGenre = findViewById<TextView>(R.id.primary_genre_game)
        val country = findViewById<TextView>(R.id.country)
        val sharedPrefs = getSharedPreferences(NAME_HISTORY_FILE_PREFERENCE, MODE_PRIVATE)
        val year =
            track?.releaseDate?.substring(0..3) //Api iTunes возвращает строку (YYYY-MM-DDTHH:MM:SSZ) забираем только Год

        url = track?.previewUrl
        artistName.text = track?.artistName
        trackName.text = track?.trackName

        preparePlayer()

        play?.setOnClickListener {
            playbackControl()
        }

        addPlayList.setOnClickListener {
            val searchHistory = SearchHistory(sharedPrefs)
            val searchHistoryList = searchHistory.read()
            searchHistory.addTrackToHistory(searchHistoryList, track!!)
        }

        Glide.with(this)
            .load(track?.artworkUrl512)
            .transform(RoundedCorners(10))
            .placeholder(R.drawable.incorrect_link312)
            .into(artwork)

        if (track?.trackTimeMillis != null) {
            trackTimeMills.text = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(track.trackTimeMillis.toLong())
        } else trackTimeMills.text = "00:00"

        collectionNameArea.isVisible = track?.collectionName != null
        collectionName.isVisible = track?.collectionName != null
        collectionName.text = track?.collectionName
        releaseDate.text = year
        primaryGenre.text = track?.primaryGenreName
        country.text = track?.country

        back.setOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        playerState = STATE_DEFAULT
    }

    private fun preparePlayer() {
        if (url != null) {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                play?.isEnabled = true
                playerState = STATE_PREPARED
            }
            mediaPlayer.setOnCompletionListener {
                play?.setImageResource(R.drawable.play)
                playerState = STATE_PREPARED
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        play?.setImageResource(R.drawable.pause)
        playerState = STATE_PLAYING

        startTimer()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        play?.setImageResource(R.drawable.play)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun startTimer() {
        handler.post(
            createUpdateTimerTask()
        )
    }

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {

                if (playerState == 2 || playerState == 3) { //Если состояние плеера Play или Pause
                    timer?.text = dateFormat.format(mediaPlayer.currentPosition)
                    handler.postDelayed(this, DELAY)
                } else {
                    timer?.text = dateFormat.format(0)
                }
            }
        }
    }
}