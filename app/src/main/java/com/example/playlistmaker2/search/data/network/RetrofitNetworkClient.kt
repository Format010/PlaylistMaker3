package com.example.playlistmaker2.search.data.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker2.search.data.NetworkClient
import com.example.playlistmaker2.search.data.dto.SearchRequest
import com.example.playlistmaker2.search.data.dto.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class RetrofitNetworkClient(
    private val connectivityManager: ConnectivityManager,
    private val imdbService: AppleItunesApi
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response(-1)
        }

        if (dto is SearchRequest) {
            return withContext(Dispatchers.IO) {
                try {
                    val response = imdbService.searchSong(dto.expression)
                    if (response.results.isEmpty()){
                        response.apply { code = 0 }
                    }else {
                        response.apply { code = 200 }
                    }
                } catch (e: IOException) {
                    Response(404)
                }
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