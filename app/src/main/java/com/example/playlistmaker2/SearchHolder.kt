package layout

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker2.R
import com.example.playlistmaker2.Track
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker2.AudioPlayer
import com.example.playlistmaker2.SearchActivity
import com.example.playlistmaker2.SearchHistory
import java.text.SimpleDateFormat
import java.util.Locale

const val AUDIO_PLAYER_DATA = "track"

class SearchHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    private val artwork: ImageView = itemView.findViewById(R.id.artwork)
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val artistName: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)
    val searchItemConstraint: View = itemView.findViewById(R.id.search_item)

    fun bind(track: Track, searchHistory: SearchHistory) {

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
        } else trackTime.text = "0:00"

        searchItemConstraint.setOnClickListener {

            val intent = Intent(it.context, AudioPlayer::class.java)
            intent.putExtra(AUDIO_PLAYER_DATA, track)
            it.context.startActivity(intent)

//            val searchHistoryList = searchHistory.read()
//            searchHistory.addTrackToHistory(searchHistoryList, track)
        }

    }
}