package com.example.playlistmaker2.presentation.ui.audio

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker2.NAME_HISTORY_FILE_PREFERENCE
import com.example.playlistmaker2.R
import com.example.playlistmaker2.SearchHistory
import com.example.playlistmaker2.data.dto.ClickDelay
import com.example.playlistmaker2.data.dto.TimeFormatting
import com.example.playlistmaker2.domain.impl.MediaPlayerImpl
import com.example.playlistmaker2.domain.models.Track
import layout.AUDIO_PLAYER_DATA

class AudioPlayer() : AppCompatActivity() {

    private var play: ImageView? = null
    private var url: String? = null
    private var timer: TextView? = null
    private lateinit var audioPlayer: MediaPlayerImpl
    private var clickDelay = ClickDelay()


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
        val timeFormatting = track?.trackTimeMillis?.let { TimeFormatting().simpleDataFormat(it) }
        audioPlayer = MediaPlayerImpl(this)

        url = track?.previewUrl

        artistName.text = track?.artistName
        trackName.text = track?.trackName

        audioPlayer.inicializePlayer(url.toString(),
            MediaPlayer.OnCompletionListener{
                play?.setImageResource(R.drawable.play)
                audioPlayer.stopTimer(timer!!)
                audioPlayer.stop()

            })

        play?.setOnClickListener {
            if (clickDelay.clickDebounce()) {
                if (!audioPlayer.isPlaying() ) { //|| playerState == STATE_PREPARED
                    audioPlayer.play()
                    play?.setImageResource(R.drawable.pause)
                    audioPlayer.startTimer(timer!!)
                } else {
                    audioPlayer.pause()
                    play?.setImageResource(R.drawable.play)
                }
            }
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

        trackTimeMills.text = timeFormatting.toString()

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
        audioPlayer.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        audioPlayer.stopTimer(timer!!)
        audioPlayer.release()
    }


}