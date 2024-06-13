package com.example.playlistmaker2.data.network

import com.example.playlistmaker2.data.NetworkClient
import com.example.playlistmaker2.data.dto.MusicSearchRequest
import com.example.playlistmaker2.data.dto.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val imdbBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(imdbBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val imdbService = retrofit.create(AppleItunesApi::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is MusicSearchRequest) {
            val resp = imdbService.searchSong(dto.expression).execute()

            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}