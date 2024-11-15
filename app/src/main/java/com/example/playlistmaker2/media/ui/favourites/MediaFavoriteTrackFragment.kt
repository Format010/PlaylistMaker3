package com.example.playlistmaker2.media.ui.favourites

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker2.AUDIO_PLAYER_DATA
import com.example.playlistmaker2.CLICK_DEBOUNCE_DELAY
import com.example.playlistmaker2.databinding.FragmentFavoriteTrackBinding
import com.example.playlistmaker2.player.ui.AudioPlayerActivity
import com.example.playlistmaker2.search.domain.model.Track
import com.example.playlistmaker2.search.ui.SearchAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaFavoriteTrackFragment: Fragment() {

    private val viewModel by viewModel<MediaFavoriteTrackViewModel>()
    private var listSong: List<Track> = emptyList()
    private var _binding: FragmentFavoriteTrackBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding wasn't initiliazed!" }
    private lateinit var rvFavourites: RecyclerView
    private lateinit var favouritesTrackAdapter: SearchAdapter
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        viewModel.favouriteTrackUiState.observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.loadDate()
        rvFavourites = binding.favouriteTrackRecyclerView
        rvFavourites.adapter = favouritesTrackAdapter
    }

    private fun render(state: FauvoriteTrackState) {
       when(state){
           is FauvoriteTrackState.Empty -> {
               binding.errorFavoriteText.isVisible = true
               binding.errorFavoriteImage.isVisible = true
               binding.favouriteTrackRecyclerView.isVisible = false
               binding.progressBar.isVisible = false
           }
           is FauvoriteTrackState.Content -> {
               binding.favouriteTrackRecyclerView.isVisible = true
               binding.errorFavoriteText.isVisible = false
               binding.errorFavoriteImage.isVisible = false
               binding.progressBar.isVisible = false
               listSong = state.data
               favouritesTrackAdapter.data = listSong
               favouritesTrackAdapter.notifyDataSetChanged()
           }
           is FauvoriteTrackState.Loading -> binding.progressBar.isVisible = true
       }
    }

    private fun init() {
        favouritesTrackAdapter = SearchAdapter(listSong){
            if (clickDebounce()) {
                val playerIntent = Intent(requireContext(), AudioPlayerActivity::class.java)
                playerIntent.putExtra(AUDIO_PLAYER_DATA, it)
                startActivity(playerIntent)
            }
        }
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        fun newInstance(): MediaFavoriteTrackFragment {
            return MediaFavoriteTrackFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}