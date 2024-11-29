package com.example.playlistmaker2.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker2.R
import com.example.playlistmaker2.databinding.PlaylistBottomSheetBinding
import com.example.playlistmaker2.media.domain.model.Playlist

class BottomSheetPlaylistAdapter(
    var playlists: List<Playlist>,
    private val listenerItem: OnClickListenerItem
) : RecyclerView.Adapter<BottomSheetPlaylistHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BottomSheetPlaylistHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_bottom_sheet, parent, false)
        return BottomSheetPlaylistHolder(view, listenerItem)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: BottomSheetPlaylistHolder, position: Int) {
        holder.bind(playlists[position])
       // holder.itemView.setOnClickListener { listenerItem.onItemClick(playlists[position]) }
    }

    fun interface OnClickListenerItem {
        fun onItemClick(playlist: Playlist)

    }
}