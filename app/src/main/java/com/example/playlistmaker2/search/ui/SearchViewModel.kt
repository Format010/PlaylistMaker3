package com.example.playlistmaker2.search.ui

import android.app.Application
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker2.SEARCH_DEBOUNCE_DELAY
import com.example.playlistmaker2.search.domain.HistoryInteractor
import com.example.playlistmaker2.search.domain.SearchInteractor
import com.example.playlistmaker2.search.domain.model.Track

class SearchViewModel(application: Application, private val searchInteractor: SearchInteractor, private val historyInteractor: HistoryInteractor): AndroidViewModel(application) {

    companion object {
        val SEARCH_REQUEST_TOKEN = Any()
    }

    private var latestSearchText: String? = null

    private val _stateLiveData = MutableLiveData<SearchState>()
    val stateLiveData: LiveData<SearchState> = _stateLiveData

    private val _historyList = MutableLiveData<List<Track>>()
    val historyList: LiveData<List<Track>> = _historyList

    var listSong: List<Track> = emptyList()
    val handler = Handler(Looper.getMainLooper())

    init {
        getHistoryListLiveData()
    }

    fun getHistoryListLiveData(){
        _historyList.postValue(historyRead())
    }


    fun searchMusicFun(newSearchText: String) {

        if (newSearchText.isNotEmpty() == true) {

            renderState(SearchState.Loading)

            searchInteractor.searchMusic(
                newSearchText,
                object : SearchInteractor.MusicConsumer {
                    override fun consume(foundMusic: List<Track>?, errorMessage: String?) {

                            if (foundMusic != null) {

                                listSong = emptyList()
                                listSong = foundMusic
                                renderState(SearchState.Content(listSong))

                            }
                            if (errorMessage != null){
                                showMessage(errorMessage)
                            } else if (listSong.isEmpty()) {
                                showMessage("Is empty")
                            } else {
                                showMessage("")
                            }
                    }
                })
        }
    }

    fun showMessage(text: String) {
        when (text) {
            "Is empty" -> {
                renderState(SearchState.Empty)

            }
            "Not connect internet" -> {
                renderState(SearchState.Error)
            }
            "Ошибка сервера" -> {

                renderState(SearchState.Error)
            }
            "404" ->{
                renderState(SearchState.Error)
            }
        }
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) return
        this.latestSearchText = changedText

        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val searchRunnable = Runnable { searchMusicFun(changedText) }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            handler.postDelayed(
                searchRunnable,
                SEARCH_REQUEST_TOKEN,
                SEARCH_DEBOUNCE_DELAY
            )
        } else {
            val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
            handler.postAtTime(
                searchRunnable,
                SEARCH_REQUEST_TOKEN,
                postTime,
            )
        }
        listSong = emptyList()
    }

    fun historyRead(): List<Track> {
        return historyInteractor.read()
    }

    fun historyWrite(searchHistory: List<Track>){
        historyInteractor.write(searchHistory)
    }

    fun historyClear(){
        historyInteractor.clearSearch()
    }

    fun historyAdd(searchHistory: List<Track>, track: Track){
        historyInteractor.addTrackToHistory(searchHistory, track)
    }


    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    private fun renderState(state: SearchState) {
        _stateLiveData.postValue(state)
    }

}