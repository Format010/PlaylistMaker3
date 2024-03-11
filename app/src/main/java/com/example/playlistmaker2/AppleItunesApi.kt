package com.example.playlistmaker2

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface AppleItunesApi {
    @GET("/search?entity=song")
    fun searchSong(@Query("term") text: String): Call<MusicResponse>

}


