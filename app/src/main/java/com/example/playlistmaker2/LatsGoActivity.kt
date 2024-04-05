package com.example.playlistmaker2

import android.content.Context
import android.content.Intent

class LatsGoActivity(private val context: Context, val track: Track) {
    fun startNewActivity(clasz: Class<*>) {

        val intent = Intent(context, clasz)
        intent.putExtra("track", track)
        context?.startActivity(intent)


    }

}