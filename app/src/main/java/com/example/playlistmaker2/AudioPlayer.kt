package com.example.playlistmaker2

import android.os.Build
import android.os.Bundle
import android.view.View
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val track =
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) {
                intent?.extras?.getParcelable(AUDIO_PLAYER_DATA, Track::class.java)
            } else {
                intent?.extras?.getParcelable(AUDIO_PLAYER_DATA)
            }

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
        val year = track?.releaseDate?.substring(0..3) //Api iTunes возвращает строку (YYYY-MM-DDTHH:MM:SSZ) забираем только Год

        artistName.text = track?.artistName
        trackName.text = track?.trackName

        addPlayList.setOnClickListener {
            val searchHistory = SearchHistory(sharedPrefs)
            val searchHistoryList = searchHistory.read()
            searchHistory.addTrackToHistory(searchHistoryList, track!!)
        }

        Glide.with(this)
            .load(track?.artworkUrl512)
            .transform(RoundedCorners(10))
            .placeholder(R.drawable.incorrect_link)
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
}