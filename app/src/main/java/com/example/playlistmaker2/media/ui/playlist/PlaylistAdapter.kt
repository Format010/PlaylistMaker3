package com.example.playlistmaker2.media.ui.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker2.databinding.PlaylistItemBinding
import com.example.playlistmaker2.media.domain.model.Playlist
import com.example.playlistmaker2.search.domain.model.Track
import com.example.playlistmaker2.search.ui.SearchAdapter.OnClickListenerItem

class PlaylistAdapter(private val listenerItem: OnClickListenerItem): RecyclerView.Adapter<PlaylistViewHolder>() {
    var playlists = ArrayList<Playlist>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(
            PlaylistItemBinding.inflate(layoutInflater, parent, false), listenerItem
        )
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    fun interface OnClickListenerItem {
        fun onItemClick(playlist: Playlist)
    }
}