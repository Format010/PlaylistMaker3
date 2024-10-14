package com.example.playlistmaker2.search.data.network

import com.example.playlistmaker2.search.data.dto.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AppleItunesApi {
    @GET("/search?entity=song")
    suspend fun searchSong(@Query("term") text: String): SearchResponse
}


