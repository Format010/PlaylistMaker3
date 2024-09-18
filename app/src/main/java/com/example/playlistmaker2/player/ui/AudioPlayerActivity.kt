package com.example.playlistmaker2.player.ui

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker2.AUDIO_PLAYER_DATA
import com.example.playlistmaker2.R
import com.example.playlistmaker2.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker2.player.domain.models.AudioPlayerStateStatus
import com.example.playlistmaker2.player.domain.models.AudioPlayerUiState
import com.example.playlistmaker2.search.domain.model.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity() : AppCompatActivity() {

    private var url: String? = null
    private lateinit var binding: ActivityAudioPlayerBinding
    private val viewModel by viewModel<AudioPlayerViewModel>() {
        parametersOf(url)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val track =
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) {
                intent?.extras?.getParcelable(AUDIO_PLAYER_DATA, Track::class.java)
            } else {
                intent?.extras?.getParcelable(AUDIO_PLAYER_DATA)
            }

        val year =
            track?.releaseDate?.substring(0..3) //Api iTunes возвращает строку (YYYY-MM-DDTHH:MM:SSZ) забираем только Год

        url = track?.previewUrl
        binding.artistName.text = track?.artistName
        binding.trackName.text = track?.trackName

        viewModel.getPlayerState.observe(this) { uiState ->
            updateUi(uiState)
        }

        val timeFormatting = formatTime(track?.trackTimeMillis.toString())

        binding.play.setOnClickListener {
            if (viewModel.clickDebounce()) {
                if (viewModel.getPlayerState.value?.isPlaying == false) {
                    viewModel.play()
                } else {
                    viewModel.pause()
                }
            }
        }

        Glide.with(this)
            .load(track?.artworkUrl512)
            .transform(RoundedCorners(10))
            .placeholder(R.drawable.incorrect_link312)
            .into(binding.artwork)

        binding.trackTimeMills.text = timeFormatting
        binding.collectionNameArea.isVisible = track?.collectionName != null
        binding.collectionName.isVisible = track?.collectionName != null
        binding.collectionName.text = track?.collectionName
        binding.releaseDate.text = year
        binding.primaryGenreGame.text = track?.primaryGenreName
        binding.country.text = track?.country

        binding.backAp.setOnClickListener {
            finish()
        }
    }

    private fun updateUi(uiState: AudioPlayerUiState) {
        if (uiState.audioPlayerStateStatus == AudioPlayerStateStatus.PLAYING || uiState.audioPlayerStateStatus == AudioPlayerStateStatus.PAUSED) binding.timing.text =
            formatTime(uiState.currentPosition.toString()) else binding.timing.text =
            formatTime("0")
        if (uiState.audioPlayerStateStatus == AudioPlayerStateStatus.DEFAULT) viewModel.preparedPlayer()
        if (uiState.isPlaying) binding.play.setImageResource(R.drawable.pause) else binding.play.setImageResource(
            R.drawable.play
        )
        if (uiState.onCompletionListener) {
            viewModel.endSong()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    fun formatTime(formatTime: String): String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(formatTime.toLong())
    }

}