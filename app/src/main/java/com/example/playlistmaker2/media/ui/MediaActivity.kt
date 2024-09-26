package com.example.playlistmaker2.media.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker2.databinding.ActivityMediaBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.example.playlistmaker2.R

class MediaActivity : AppCompatActivity() {

    private var tabMediator: TabLayoutMediator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            finish()
        }

        binding.viewPager.adapter = MediaNumbersViewPagerAdapter(supportFragmentManager,lifecycle)


        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_track)
                else -> tab.text = getString(R.string.playlist)
            }
        }
        tabMediator?.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator?.detach()
    }
}