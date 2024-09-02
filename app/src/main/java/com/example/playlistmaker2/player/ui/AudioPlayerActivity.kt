package com.example.playlistmaker2.player.ui

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker2.AUDIO_PLAYER_DATA
import com.example.playlistmaker2.R
import com.example.playlistmaker2.search.domain.model.Track
import com.example.playlistmaker2.util.CreatorSearch

class AudioPlayerActivity() : AppCompatActivity() {

    private var play: ImageView? = null
    private var url: String? = null
    private var timer: TextView? = null
    private var timerOn: Boolean = false
    private var searchHistory = CreatorSearch.provideHistoryInteractor()
    private lateinit var viewModel: AudioPlayerViewModel

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
        val year =
            track?.releaseDate?.substring(0..3) //Api iTunes возвращает строку (YYYY-MM-DDTHH:MM:SSZ) забираем только Год

        url = track?.previewUrl
        artistName.text = track?.artistName
        trackName.text = track?.trackName

        viewModel = ViewModelProvider(this,
            AudioPlayerViewModel.getViewModelFactory(url)
        )[AudioPlayerViewModel::class.java]

        val timeFormatting = viewModel.formatTime(track?.trackTimeMillis.toString())



        viewModel.currentPosition.observe(this) {  position ->
            timer?.text = viewModel.formatTime(position.toString())
        }


        viewModel.preparedPlayer()

        play?.setOnClickListener {
            if (viewModel.clickDebounce()) {
                if (viewModel.isPlaying.value == false) {
                    viewModel.play()
                    play?.setImageResource(R.drawable.pause)
                    timer?.let { viewModel.startTimer(it)}
                    timerOn = true
                } else {
                    viewModel.pause()
                    play?.setImageResource(R.drawable.play)
                }
            }
        }

        if (searchHistory.read().contains(track)) addPlayList?.setImageResource(R.drawable.check)
        else addPlayList?.setImageResource(R.drawable.check_2)

        addPlayList.setOnClickListener {
            val searchHistoryList = searchHistory.read()
            searchHistoryList.let { it1 -> searchHistory.addTrackToHistory(it1, track!!) }
            addPlayList?.setImageResource(R.drawable.check)
        }

        Glide.with(this)
            .load(track?.artworkUrl512)
            .transform(RoundedCorners(10))
            .placeholder(R.drawable.incorrect_link312)
            .into(artwork)

        trackTimeMills.text = timeFormatting

        collectionNameArea.isVisible = track?.collectionName != null
        collectionName.isVisible = track?.collectionName != null
        collectionName.text = track?.collectionName
        releaseDate.text = year
        primaryGenre.text = track?.primaryGenreName
        country.text = track?.country

        back.setOnClickListener {
            finish()
        }

        viewModel.setOnCompletionListener{
            viewModel.stop()
            play?.setImageResource(R.drawable.play)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (timerOn) {
            timer?.let { viewModel.stopTimer(it) }
        }
        viewModel.release()
    }

}