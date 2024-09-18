package com.example.playlistmaker2.search.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker2.EDITED_TEXT
import com.example.playlistmaker2.R
import com.example.playlistmaker2.search.domain.model.Track
import com.example.playlistmaker2.util.CreatorSearch
import com.google.gson.Gson
import androidx.core.widget.addTextChangedListener
import com.example.playlistmaker2.USER_KEY_HISTORY

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val TEXT = ""
    }

    private lateinit var viewModel: SearchViewModel

    private var listSong: List<Track> = emptyList()
    private var textValue: String = TEXT
    lateinit var historyLayout: LinearLayout
    lateinit var inputEditText: EditText
    lateinit var rvSearch: RecyclerView
    lateinit var progressBar: View
    lateinit var songAdapter: SearchAdapter
    lateinit var clearButton: ImageView
    private var placeholderMessage: ViewGroup? = null
    private var placeholderMessage2: ViewGroup? = null
    private val gson = Gson()
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        sharedPrefs = getSharedPreferences(USER_KEY_HISTORY, MODE_PRIVATE)
        val history = CreatorSearch.provideHistoryInteractor(sharedPrefs, gson)
        viewModel = ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory()
        )[SearchViewModel::class.java]

        viewModel.observeState().observe(this) {
            render(it)
        }

        val back = findViewById<Button>(R.id.back_search)
        placeholderMessage = findViewById(R.id.placeholder_message)
        placeholderMessage2 = findViewById(R.id.placeholder_message2)
        historyLayout = findViewById(R.id.history_layout)
        inputEditText = findViewById(R.id.input_search)
        rvSearch = findViewById(R.id.searchRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        songAdapter = SearchAdapter(listSong, sharedPrefs, gson)
        clearButton = findViewById(R.id.clear_text)
        val rvHistory = findViewById<RecyclerView>(R.id.historyRecyclerView)
        val update = findViewById<Button>(R.id.update)
        val clearHistoryButton = findViewById<Button>(R.id.clear_history)

        rvSearch.adapter = songAdapter
        rvHistory.adapter = SearchAdapter(history.read(), sharedPrefs, gson)
        if (history.read().isNotEmpty()) historyLayout.isVisible = true

        if (savedInstanceState != null) {
            textValue = savedInstanceState.getString(
                EDITED_TEXT,
                TEXT
            )
            inputEditText.setText(textValue)
        }

        inputEditText.addTextChangedListener(onTextChanged = { s: CharSequence?, _, _, _ ->
            if (placeholderMessage2?.isVisible == true) {
                placeholderMessage2?.isVisible = false
            }
            if (placeholderMessage?.isVisible == true) {
                placeholderMessage?.isVisible = false
            }
            viewModel.searchDebounce(changedText = s?.toString() ?: "")

            clearButton.isVisible = !s.isNullOrEmpty()
            historyLayout.isVisible = if (history.read().isEmpty()) false else true
            historyLayout.isVisible =
                if (inputEditText.hasFocus() && s?.isEmpty() == true) true else false

        },
            afterTextChanged = { a: Editable? ->
                textValue = a.toString()
            })

        back.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            inputEditText.setText(TEXT)
            val view: View? = currentFocus
            if (view != null) {
                val inputMethodManager =
                    this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                songAdapter.data = emptyList()

                songAdapter.notifyDataSetChanged()
                rvHistory.adapter = SearchAdapter(history.read(), sharedPrefs, gson)
                if (placeholderMessage2?.isVisible == true) {
                    placeholderMessage2?.isVisible = false
                }
                if (placeholderMessage?.isVisible == true) {
                    placeholderMessage?.isVisible = false
                }
            }
        }

        update.setOnClickListener {
            viewModel.searchMusicFun(inputEditText.text.toString())
            songAdapter.notifyDataSetChanged()
            if (placeholderMessage2?.isVisible == true) {
                placeholderMessage2?.isVisible = false
            }
        }

        clearHistoryButton.setOnClickListener {
            history.clearSearch()
            rvHistory.adapter = SearchAdapter(history.read(), sharedPrefs, gson)
            historyLayout.isVisible = false
        }
    }

    private fun showLoading() {
        placeholderMessage2?.visibility = View.GONE
        placeholderMessage?.visibility = View.GONE
        historyLayout.visibility = View.GONE
        rvSearch.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun showEmpty() {
        progressBar.visibility = View.GONE
        placeholderMessage2?.visibility = View.GONE
        placeholderMessage?.visibility = View.VISIBLE
    }

    private fun showError() {
        progressBar.visibility = View.GONE
        placeholderMessage2?.visibility = View.VISIBLE
    }

    private fun showContent(song: List<Track>) {
        progressBar.visibility = View.GONE
        placeholderMessage?.visibility = View.GONE
        placeholderMessage2?.visibility = View.GONE
        rvSearch.visibility = View.VISIBLE

        songAdapter.data = emptyList()
        //songAdapter.data.clear()
        songAdapter.data = song
        songAdapter.notifyDataSetChanged()
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Loading -> showLoading()
            is SearchState.Error -> showError()
            is SearchState.Empty -> showEmpty()
            is SearchState.Content -> showContent(state.song)
        }
    }

}