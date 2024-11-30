package com.example.playlistmaker2.media.ui.playlist

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker2.R
import com.example.playlistmaker2.media.domain.PlaylistInteractor
import com.example.playlistmaker2.media.domain.model.Playlist
import com.example.playlistmaker2.search.domain.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class PlaylistScreenViewModel(val context: Context, val interactor: PlaylistInteractor, val gson: Gson): ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistScreenState>()
    val stateLive: LiveData<PlaylistScreenState> = stateLiveData
    lateinit var playlistChanges: Playlist
    var listId: List<String> = emptyList()

    fun getNewPLaylist(plalistId: Int){
        viewModelScope.launch {
            val playlistNew = interactor.getPLaylistById(plalistId)
            playlistChanges = playlistNew
            setPlaylist(playlistNew)
        }
    }

    fun setPlaylist(playlist: Playlist){
        listId = gson.fromJson(playlist.trackId, Array<String>::class.java).toMutableList()
        getTracklists(listId)
    }

    private fun getTracklists(listId: List<String>){
        viewModelScope.launch {
            interactor.getTrackListById(listId)
                .onStart { render(PlaylistScreenState.Loading) }
                .collect{trackList ->
                    showData(trackList)
                }
        }
    }

    private fun showData(trackList: List<Track>){
        if (trackList.isEmpty()){
            render(PlaylistScreenState.Content(emptyList(), playlistChanges))
        }else{
            render(PlaylistScreenState.Content(trackList, playlistChanges))
        }
    }

    private fun render(state: PlaylistScreenState){
        stateLiveData.postValue(state)
    }

    fun delTrackInPlaylist(trackId: String, playlist: Playlist){
        viewModelScope.launch {
            var remainingTrackall = emptyList<String>()
            val allPLaylist = interactor.getAllPlaylists().first() //Получаем все плейлисты
            val otherPLayLists = allPLaylist.filter { it != playlist } //Забираем все плейлисты кроме текущего
            val allTrackOtherPLaylists = mutableListOf<String>()
            otherPLayLists.forEach{ playlist ->    //Берем все треки из других плейлистов для сверки
                val trackId = gson.fromJson(playlist.trackId, Array<String>::class.java)
                allTrackOtherPLaylists.addAll(trackId)
            }
            remainingTrackall = allTrackOtherPLaylists.filter { it == trackId } //Фильтруем по ID удаляемый трек и ID треков с других плейлистов
            delTrackList(remainingTrackall, trackId, playlist)
        }
    }

    private fun delTrackList(trackListByOtherPlaylist: List<String>, trackId: String, playlist: Playlist){

        if (trackListByOtherPlaylist.isNotEmpty()){
            viewModelScope.launch {
               interactor.delTrackIdToPlaylist(trackId, playlist)
                getNewPLaylist(playlist.playlistId)
            }
        }else  {
            viewModelScope.launch {
                interactor.deleteTrackFromTable(trackId)
                interactor.delTrackIdToPlaylist(trackId, playlist)
                getNewPLaylist(playlist.playlistId)
            }
        }
    }

    fun delPlayList(playlist: Playlist) {
        val id = gson.fromJson(playlist.trackId, Array<String>::class.java)

        viewModelScope.launch {
            val allPLaylist = interactor.getAllPlaylists().first() //Получаем все плейлисты

            val otherPLayLists = allPLaylist.filter { it != playlist } //Забираем все плейлисты кроме удаляемого
            val remainingTrack = mutableListOf<String>()

            otherPLayLists.forEach{ playlist ->    //Берем все треки из других плейлистов для сверки
                val trackId = gson.fromJson(playlist.trackId, Array<String>::class.java)
                remainingTrack.addAll(trackId)
            }
            val trackToRemove = id.filter { it !in remainingTrack } //Оставляем только те треки которых нет в других плейлистах

            trackToRemove.forEach{ it -> interactor.deleteTrackFromTable(it) } //Удаляем треки которых нет в других плейлистах
            interactor.deletePlaylist(playlist)
            render(PlaylistScreenState.Delite)
        }
    }

    fun sharePlaylistText(): String {
        val playlistModel =
            (stateLive.value as? PlaylistScreenState.Content)?.playlist
        val tracks =
            (stateLive.value as? PlaylistScreenState.Content)?.trackList ?: emptyList()

        val trackListText = tracks.mapIndexed { index, track ->
            "${index + 1}. ${track.artistName} - ${track.trackName} (${formatTime(track.trackTimeMillis.toString())})"
        }.joinToString("\n")
        return """
            |${playlistModel?.title}
            |${playlistModel?.description}
            |${playlistModel?.trackCount?.let {
            context.resources?.getQuantityString(R.plurals.count_of_tracks,
                it, playlistModel.trackCount)
        }} 
            |
            |$trackListText
        """.trimMargin()
    }

    private fun formatTime(formatTime: String): String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(formatTime.toLong())
    }


}