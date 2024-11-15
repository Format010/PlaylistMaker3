package com.example.playlistmaker2.media.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.playlistmaker2.databinding.FragmentPlaylistBinding

class MediaPlaylistFragment: Fragment() {

    private val viewModel: MediaPlaylistViewModel by viewModels()

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding wasn't initiliazed!" }

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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): MediaPlaylistFragment {
            return MediaPlaylistFragment()
        }

    }
}