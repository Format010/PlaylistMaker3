package com.example.playlistmaker2.media.ui
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker2.media.ui.favourites.MediaFavoriteTrackFragment
import com.example.playlistmaker2.media.ui.playlist.PlaylistFragment


class MediaNumbersViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> MediaFavoriteTrackFragment.newInstance()
            else -> PlaylistFragment.newInstance()
        }
    }
}