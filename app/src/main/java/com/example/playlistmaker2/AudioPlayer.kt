package com.example.playlistmaker2


import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import layout.AUDIO_PLAYER_DATA

class AudioPlayer() : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

//        val track =
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) { // TIRAMISU onwards
//                intent?.extras?.getParcelable(AUDIO_PLAYER_DATA, Track::class.java)
//            } else {
//                intent?.extras?.getParcelable(AUDIO_PLAYER_DATA)
//            }

        val track: Track = intent.getParcelableExtra(AUDIO_PLAYER_DATA)!!

        val back = findViewById<Button>(R.id.back_ap)
        val artwork = findViewById<ImageView>(R.id.artwork)
        val trackName = findViewById<TextView>(R.id.track_name)
        val artistName = findViewById<TextView>(R.id.artist_name)
        val addPlayList = findViewById<ImageView>(R.id.check)
        val addFavorite = findViewById<ImageView>(R.id.favorite)
        val trackTimeMills = findViewById<TextView>(R.id.track_time_mills)
        val collectionName = findViewById<TextView>(R.id.collection_name)
        val releaseDate = findViewById<TextView>(R.id.release_date)
        val primaryGenre = findViewById<TextView>(R.id.primary_genre_game)
        val country = findViewById<TextView>(R.id.country)

        artistName.text = track.artistName

//        Glide.with(this)
//            .load(track?.artworkUrl100?.replaceAfterLast('/',"512x512bb.jpg"))
//            .transform(RoundedCorners(10))
//            .placeholder(R.drawable.incorrect_link)
//            .into(artwork)
//       trackName.text = track?.trackName


        back.setOnClickListener {
            finish()
        }
    }
}