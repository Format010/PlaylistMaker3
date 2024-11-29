package com.example.playlistmaker2.media.ui.playlist

import android.content.Context
import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker2.R
import com.example.playlistmaker2.databinding.PlaylistItemBinding
import com.example.playlistmaker2.media.domain.model.Playlist

class PlaylistViewHolder(
    private val binding: PlaylistItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    private val trackEnding = itemView.context.resources

    fun bind(item: Playlist) {
        val filePath = item.filePath
        Glide.with(itemView)
            .load(filePath)
            .placeholder(R.drawable.incorrect_link312)
            .transform(
                CenterCrop(), RoundedCorners(dpToPx(8f, itemView.context))
            )
            .into(binding.artwork)

        binding.title.text = item.title
        binding.description.text = changingTracksCountEnding(item.trackCount)

    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    private fun changingTracksCountEnding(tracksCount: Int?): String {
        val defaultEnding = itemView.context.getString(R.string.track_name)
        return if (tracksCount == null) {
            defaultEnding
        } else {
            return trackEnding.getQuantityString(R.plurals.count_of_tracks, tracksCount,tracksCount)
        }
    }
}