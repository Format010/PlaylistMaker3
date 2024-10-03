package com.example.playlistmaker2.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker2.R
import com.example.playlistmaker2.databinding.FragmentMediaBinding
import com.google.android.material.tabs.TabLayoutMediator


class MediaFragment : Fragment() {

    private lateinit var binding: FragmentMediaBinding
    private var tabMediator: TabLayoutMediator? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = MediaNumbersViewPagerAdapter(childFragmentManager,lifecycle)


        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_track)
                else -> tab.text = getString(R.string.playlist)
            }
        }
        tabMediator?.attach()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator?.detach()
    }


}