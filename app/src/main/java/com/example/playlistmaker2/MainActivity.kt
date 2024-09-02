package com.example.playlistmaker2

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker2.presentation.ui.audio.SearchActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<View>(R.id.search)
        val media = findViewById<View>(R.id.media)
        val settings = findViewById<View>(R.id.settings)

       searchButton.setOnClickListener{
           val displayIntentSearch = Intent(this, SearchActivity::class.java)
           startActivity(displayIntentSearch)
       }
        media.setOnClickListener{
            val displayIntentMedia = Intent(this, MediaActivity::class.java)
            startActivity(displayIntentMedia)
        }
        settings.setOnClickListener{
            val displayIntentSettings = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntentSettings)
        }
    }
}