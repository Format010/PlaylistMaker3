package com.example.playlistmaker2.search.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker2.SEARCH_DEBOUNCE_DELAY
import com.example.playlistmaker2.search.domain.HistoryInteractor
import com.example.playlistmaker2.search.domain.SearchInteractor
import com.example.playlistmaker2.search.domain.model.Track
import com.example.playlistmaker2.util.debounce
import kotlinx.coroutines.launch

class SearchViewModel(application: Application, private val searchInteractor: SearchInteractor, private val historyInteractor: HistoryInteractor): AndroidViewModel(application) {

    private var latestSearchText: String? = null

    private val _stateLiveData = MutableLiveData<SearchState>()
    val stateLiveData: LiveData<SearchState> = _stateLiveData

    private val _historyList = MutableLiveData<List<Track>>()
    val historyList: LiveData<List<Track>> = _historyList

    var listSong: List<Track> = emptyList()

    private val musicSearchDebounce = debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
        searchMusicFun(changedText)
    }

    init {
        getHistoryListLiveData()
    }

    fun getHistoryListLiveData(){
        _historyList.postValue(historyRead())
    }

    fun searchMusicFun(newSearchText: String) {

        if (newSearchText.isNotEmpty()) {

            renderState(SearchState.Loading)

            viewModelScope.launch {
                searchInteractor
                    .searchMusic(newSearchText)
                    .collect {pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundMusic: List<Track>?, errorMessage: String?) {

        if (foundMusic != null) {
            listSong = emptyList()
            listSong = foundMusic
            renderState(SearchState.Content(listSong))
        }

        when (errorMessage) {
            "Is empty" -> {
                renderState(SearchState.Empty)
            }
            "Not connect internet" -> {
                renderState(SearchState.Error)
            }
            "Ошибка сервера" -> {

                renderState(SearchState.Error)
            }
            "404" -> {
                renderState(SearchState.Error)
            }
        }
    }

    fun searchDebounce(changedText: String) {
            if (latestSearchText != changedText) {
            latestSearchText = changedText
            musicSearchDebounce(changedText)
        }
        listSong = emptyList()
    }

    fun historyRead(): List<Track> {
        return historyInteractor.read()
    }

    fun historyClear(){
        historyInteractor.clearSearch()
    }

    fun historyAdd(searchHistory: List<Track>, track: Track){
        historyInteractor.addTrackToHistory(searchHistory, track)
    }

    private fun renderState(state: SearchState) {
        _stateLiveData.postValue(state)
    }

}