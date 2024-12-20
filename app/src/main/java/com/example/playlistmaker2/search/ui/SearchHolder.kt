package layout

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker2.R
import com.example.playlistmaker2.search.domain.model.Track
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker2.search.ui.SearchAdapter
import java.text.SimpleDateFormat
import java.util.Locale

class SearchHolder(
                    itemView: View,
                    private val listenerItem: SearchAdapter.OnClickListenerItem,
                    private val longListenerItem: SearchAdapter.OnLongClickListener?
) : RecyclerView.ViewHolder(itemView) {

    private val artwork: ImageView = itemView.findViewById(R.id.artwork)
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val trackTime: TextView = itemView.findViewById(R.id.num_tracks)

    fun bind(track: Track) {

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .transform(RoundedCorners(10))
            .placeholder(R.drawable.incorrect_link)
            .into(artwork)
        trackName.text = track.trackName
        artistName.text = track.artistName

        if (track.trackTimeMillis != null) {
            trackTime.text = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(track.trackTimeMillis.toLong())
        } else trackTime.text = SimpleDateFormat().format(0)

        itemView.setOnClickListener {
            listenerItem.onItemClick(track)
        }
        itemView.setOnLongClickListener {
            longListenerItem?.onLongItemClick(track)
            true
        }

    }


}