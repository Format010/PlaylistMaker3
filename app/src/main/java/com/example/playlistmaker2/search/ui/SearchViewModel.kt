package com.example.playlistmaker2.search.ui


import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker2.SEARCH_DEBOUNCE_DELAY
import com.example.playlistmaker2.search.domain.SearchInteractor
import com.example.playlistmaker2.search.domain.model.Track
import com.example.playlistmaker2.util.CreatorSearch
import java.util.LinkedList

class SearchViewModel(application: Application): AndroidViewModel(application) {

    companion object {
        val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }
    //Передаем в RetrofitNetworkClient для функции которая узнает есть ли подключение к интернету
    private val connectivityManager = application.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val musicInteractor = CreatorSearch.provideMusicInteractor(connectivityManager)
    private var latestSearchText: String? = null
    private val stateLiveData = MutableLiveData<SearchState>()

    val listSong = LinkedList<Track>()
    val handler = Handler(Looper.getMainLooper())

    fun observeState(): LiveData<SearchState> = stateLiveData

    fun searchMusicFun(newSearchText: String) {

        if (newSearchText.isNotEmpty() == true) {

            renderState(SearchState.Loading)

            musicInteractor.searchMusic(
                newSearchText,
                object : SearchInteractor.MusicConsumer {
                    override fun consume(foundMusic: List<Track>?, errorMessage: String?) {

                            if (foundMusic != null) {

                                listSong.clear()
                                listSong.addAll(foundMusic)
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

                renderState(SearchState.Empty)
            }
            "404" ->{
                renderState(SearchState.Empty)
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
        listSong.clear()
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

}