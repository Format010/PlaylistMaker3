package com.example.playlistmaker2.search.data.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker2.search.data.NetworkClient
import com.example.playlistmaker2.search.data.dto.SearchRequest
import com.example.playlistmaker2.search.data.dto.Response
import java.io.IOException

class RetrofitNetworkClient(
    private val connectivityManager: ConnectivityManager,
    private val imdbService: AppleItunesApi
) : NetworkClient {

    override fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response(-1)
        }
        if (dto is SearchRequest) {
            try {
                val resp = imdbService.searchSong(dto.expression).execute()
                if (resp.isSuccessful) {
                    val body = resp.body() ?: Response(resp.code())
                    body.code = resp.code()
                    return body
                } else {
                    return Response(resp.code())
                }
            } catch (e: IOException) {
                return Response(404)
            }
        } else {
            return Response(400)
        }

    }

    private fun isConnected(): Boolean {

        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

}