package com.example.playlistmaker2.media.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker2.R
import com.example.playlistmaker2.databinding.FragmentPlaylistBinding
import com.example.playlistmaker2.media.domain.model.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment: Fragment() {

    private val viewModel by viewModel<PlaylistViewModel>()

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding wasn't initiliazed!" }
    private var adapter: PlaylistAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerviewPlaylist.layoutManager = GridLayoutManager(requireContext(), 2)

        adapter = PlaylistAdapter(){
            val bundle = Bundle().apply {
                putInt("playlistId", it.playlistId)
            }
            findNavController().navigate(R.id.action_mediaFragment_to_playlistScreenFragment, bundle)
        }

        binding.recyclerviewPlaylist.adapter = adapter

        viewModel.getPlaylists()
        viewModel.stateLive.observe(viewLifecycleOwner){
            render(it)
        }

        binding.newPlaylist.setOnClickListener {
            val bundle = Bundle().apply {
                putString("playlist", "Fragment")
            }
            findNavController().navigate(R.id.action_mediaFragment_to_createPlaylistFragment, bundle)
        }
    }

    private fun render(state: PlaylistState) {
        when (state) {
            is PlaylistState.Content -> showContent(state.playlists)
            is PlaylistState.Empty -> showEmpty(state.message)
            PlaylistState.Loading -> showLoading()
            else -> {}
        }
    }

    private fun showContent(playlists: List<Playlist>) {
        binding.progressBar.visibility = View.GONE
        binding.errorPlaylistImage.visibility = View.GONE
        binding.errorPlaylistText.visibility = View.GONE

        binding.recyclerviewPlaylist.visibility = View.VISIBLE
        adapter?.playlists?.clear()
        adapter?.playlists?.addAll(playlists)
        adapter?.notifyDataSetChanged()

    }

    private fun showEmpty(message:String) {
        binding.progressBar.visibility = View.GONE
        binding.recyclerviewPlaylist.visibility = View.GONE
        binding.errorPlaylistImage.visibility = View.VISIBLE
        binding.errorPlaylistText.visibility = View.VISIBLE
        binding.errorPlaylistText.text = message
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.errorPlaylistImage.visibility = View.GONE
        binding.errorPlaylistText.visibility = View.GONE
        binding.recyclerviewPlaylist.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): PlaylistFragment {
            return PlaylistFragment()
        }
    }

}