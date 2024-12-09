package com.example.playlistmaker2.player.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker2.AUDIO_PLAYER_DATA
import com.example.playlistmaker2.R
import com.example.playlistmaker2.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker2.media.domain.model.Playlist
import com.example.playlistmaker2.media.ui.playlist.CreatePlaylistFragment
import com.example.playlistmaker2.player.domain.models.AudioPlayerStateStatus
import com.example.playlistmaker2.player.domain.models.AudioPlayerUiState
import com.example.playlistmaker2.search.domain.model.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity() : AppCompatActivity(), audioPlayerToFragment  {

    private var url: String? = null
    private var idTrack: String? = null
    private lateinit var binding: ActivityAudioPlayerBinding
    private var adapter: BottomSheetPlaylistAdapter? = null
    private var track: Track? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private val viewModel by viewModel<AudioPlayerViewModel>() {
        parametersOf(url, idTrack)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomSheetContainer = binding.standardBottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }


        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                        adapter?.notifyDataSetChanged()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset + 1
            }
        })

         track =
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) {
                intent?.extras?.getParcelable(AUDIO_PLAYER_DATA, Track::class.java)
            } else {
                intent?.extras?.getParcelable(AUDIO_PLAYER_DATA)
            }

        val year =
            track?.releaseDate?.substring(0..3) //Api iTunes возвращает строку (YYYY-MM-DDTHH:MM:SSZ) забираем только Год

        url = track?.previewUrl
        idTrack = track?.trackId
        binding.artistName.text = track?.artistName
        binding.trackName.text = track?.trackName
        binding.rvBottomSheet.adapter = adapter

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

        binding.check.setOnClickListener {
            viewModel.getPlaylist()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
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

        binding.buttonNewPlaylist.setOnClickListener {
            openFragment()
            binding.containerViewAudiopleer.isVisible = true
            binding.scroll.isVisible = false
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.backAp.setOnClickListener {
            finish()
        }
        binding.favorite.setOnClickListener{
            if (viewModel.clickDebounce())viewModel.onFavoriteClicked(track!!)
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
        if (uiState.favouritesTrack)binding.favorite.setImageResource(R.drawable.like_2)
        else binding.favorite.setImageResource(R.drawable.like)

        when (uiState.playlistState) {
             is BottomSheetState.Loading -> showLoading()
             is BottomSheetState.Empty -> showEmpty()
             is BottomSheetState.Content -> showContent(uiState.playlistState.song)
            else -> {}
        }
        when (uiState.insertTrackState){
            is BottomSheetState.IsertTrack -> toast(uiState.insertTrackState.platListName, uiState.insertTrackState.result)
            else -> return
        }
    }

    @SuppressLint("StringFormatMatches")
    private fun toast(message: String, result: Boolean) {
        val textToastTrue = getString(R.string.message_toast_insert_playlist, message)
        val textToastFalse = getString(R.string.message_toast_insert_playlist_false, message)

        if (result){
            Toast.makeText(this, textToastTrue , Toast.LENGTH_LONG).show()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }else Toast.makeText(this, textToastFalse, Toast.LENGTH_LONG).show()
    }

    private fun showContent(data: List<Playlist>){

        adapter = BottomSheetPlaylistAdapter(data){
            track?.let { it1 -> viewModel.addTrackToPLaylist(it1, it) }

        }

        adapter?.playlists = emptyList()
        adapter?.playlists = data
        binding.rvBottomSheet.adapter = adapter
        binding.standardBottomSheet.isVisible = true
        binding.titleBottomSheet.isVisible = true
        binding.buttonNewPlaylist.isVisible = true
        binding.rvBottomSheet.isVisible = true
        binding.progressBarBS.isVisible = false
        adapter?.notifyDataSetChanged()

    }

    private fun showEmpty() {
        binding.standardBottomSheet.isVisible = true
        binding.titleBottomSheet.isVisible = true
        binding.buttonNewPlaylist.isVisible = true
        binding.rvBottomSheet.isVisible = false
        binding.progressBarBS.isVisible = false
    }

    private fun showLoading() {
        binding.standardBottomSheet.isVisible = true
        binding.titleBottomSheet.isVisible = false
        binding.buttonNewPlaylist.isVisible = false
        binding.rvBottomSheet.isVisible = false
        binding.progressBarBS.isVisible = true
    }

    private fun openFragment() {

        val fragment = CreatePlaylistFragment.newInstance("AudioPlayer")
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_view_audiopleer, fragment)
            .addToBackStack(null)
            .commit()

    }

    override fun showElementUi(){
        binding.scroll.isVisible = true
        binding.containerViewAudiopleer.isVisible = false
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    private fun formatTime(formatTime: String): String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(formatTime.toLong())
    }

}

interface audioPlayerToFragment {
    fun showElementUi()
}