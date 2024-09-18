package com.example.playlistmaker2.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker2.media.ui.MediaActivity
import com.example.playlistmaker2.R
import com.example.playlistmaker2.search.ui.SearchActivity
import com.example.playlistmaker2.settings.ui.SettingsActivity


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