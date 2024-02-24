package layout

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker2.R
import com.example.playlistmaker2.Track
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class SearchHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val rootLayout: LinearLayout = itemView.findViewById(R.id.rootLayout)
    private val artwork: ImageView = itemView.findViewById(R.id.artwork)
    private val track_name: TextView = itemView.findViewById(R.id.track_name)
    private val artist_name: TextView = itemView.findViewById(R.id.artist_name)
    private val track_time: TextView = itemView.findViewById(R.id.track_time)

    fun bind(track: Track){
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.incorrect_link)
            .transform(RoundedCorners(10))
            .into(artwork)
        track_name.text = track.trackName
        artist_name.text = track.artistName
        track_time.text = track.trackTime
    }

}