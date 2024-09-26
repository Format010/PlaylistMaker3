package com.example.playlistmaker2.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker2.databinding.FragmentFavoriteTrackBinding
import com.example.playlistmaker2.media.presentation.MediaFavoriteTrackViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaFavoriteTrackFragment: Fragment() {

    private val viewModel: MediaFavoriteTrackViewModel by viewModel()
    private var _binding: FragmentFavoriteTrackBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): MediaFavoriteTrackFragment {
            return MediaFavoriteTrackFragment()
        }
    }



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

    }
}