package com.example.playlistmaker2.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.playlistmaker2.databinding.FragmentPlaylistBinding
import com.example.playlistmaker2.media.presentation.MediaPlaylistViewModel

class MediaPlaylistFragment: Fragment() {

    private val viewModel: MediaPlaylistViewModel by viewModels()

    companion object {
        fun newInstance(): MediaPlaylistFragment {
            return MediaPlaylistFragment()
        }

    }

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}