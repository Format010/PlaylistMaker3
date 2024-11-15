package com.example.playlistmaker2.player.ui

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker2.R
import com.example.playlistmaker2.media.domain.model.Playlist

class BottomSheetPlaylistHolder(itemView: View, private val listenerItem: BottomSheetPlaylistAdapter.OnClickListenerItem
) : RecyclerView.ViewHolder(itemView) {

    private val ivCoverIcon = itemView.findViewById<ImageView>(R.id.cover_icon)
    private val trackEnding = itemView.context.resources
    private val name = itemView.findViewById<TextView>(R.id.name)
    private val numTrack = itemView.findViewById<TextView>(R.id.numTracks)

    fun bind(playlist: Playlist) {
        val url = playlist.filePath

        Glide.with(itemView)
            .load(url)
            .placeholder(R.drawable.incorrect_link312)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(2f, itemView.context)))
            .into(ivCoverIcon)

         name.text = playlist.title
         numTrack.text = changingTracksCountEnding(playlist.trackCount)

        itemView.setOnClickListener {
            listenerItem.onItemClick(playlist = playlist)
        }

    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    private fun changingTracksCountEnding(tracksCount: Int?): String {
        val defaultEnding = itemView.context.getString(R.string.tracks_zero)
        return if (tracksCount == null) {
            defaultEnding
        } else {
            return trackEnding.getQuantityString(R.plurals.count_of_tracks, tracksCount,tracksCount)
        }
    }

}