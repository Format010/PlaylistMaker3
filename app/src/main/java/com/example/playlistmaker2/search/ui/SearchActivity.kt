package com.example.playlistmaker2.search.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker2.EDITED_TEXT
import com.example.playlistmaker2.R
import com.example.playlistmaker2.search.domain.model.Track
import androidx.core.widget.addTextChangedListener
import com.example.playlistmaker2.AUDIO_PLAYER_DATA
import com.example.playlistmaker2.CLICK_DEBOUNCE_DELAY
import com.example.playlistmaker2.player.ui.AudioPlayerActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val TEXT = ""
    }
    private val viewModel by viewModel<SearchViewModel>()
    private var listSong: List<Track> = emptyList()
    private var textValue: String = TEXT
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var historyLayout: LinearLayout
    private lateinit var inputEditText: EditText
    private lateinit var rvSearch: RecyclerView
    private lateinit var progressBar: View
    private lateinit var songAdapter: SearchAdapter
    private lateinit var rvHistory: RecyclerView
    private lateinit var historyAdapter: SearchAdapter
    private lateinit var clearButton: ImageView
    private var placeholderMessage: ViewGroup? = null
    private var placeholderMessage2: ViewGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        viewModel.stateLiveData.observe(this) {
            render(it)
        }

        val back = findViewById<Button>(R.id.back_search)
        placeholderMessage = findViewById(R.id.placeholder_message)
        placeholderMessage2 = findViewById(R.id.placeholder_message2)
        historyLayout = findViewById(R.id.history_layout)
        inputEditText = findViewById(R.id.input_search)
        progressBar = findViewById(R.id.progressBar)
        clearButton = findViewById(R.id.clear_text)
        rvSearch = findViewById(R.id.searchRecyclerView)
        rvHistory = findViewById(R.id.historyRecyclerView)

        init() //Инициализация добавления трека в историю и переход в плеер

        rvSearch.adapter = songAdapter
        rvHistory.adapter = historyAdapter
        val update = findViewById<Button>(R.id.update)
        val clearHistoryButton = findViewById<Button>(R.id.clear_history)


        viewModel.historyList.observe(this){
                historyList ->
                historyAdapter.data = historyList
                rvHistory.adapter = historyAdapter
                historyAdapter.notifyDataSetChanged()
                if (historyList.isNotEmpty()) historyLayout.isVisible = true
        }

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
            historyLayout.isVisible = viewModel.historyRead().isNotEmpty()
            historyLayout.isVisible =
                /*inputEditText.hasFocus() &&*/ s?.isEmpty() == true //при повороте экрана делается фокус на строку ввода текста, как этого избежать не знаю

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
                historyAdapter.data = viewModel.historyRead()
                historyAdapter.notifyDataSetChanged()
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
            viewModel.historyClear()
            historyAdapter.data = viewModel.historyRead()
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
        songAdapter.data = song
        songAdapter.notifyDataSetChanged()
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun init() {
        historyAdapter = SearchAdapter(viewModel.historyRead()){
            if (clickDebounce()) {
                val playerIntent = Intent(this@SearchActivity, AudioPlayerActivity::class.java)
                playerIntent.putExtra(AUDIO_PLAYER_DATA, it)
                startActivity(playerIntent)
            }
        }

        songAdapter = SearchAdapter(listSong){
            if (clickDebounce()) {
                viewModel.historyAdd(viewModel.historyRead(), it)
                val playerIntent = Intent(this@SearchActivity, AudioPlayerActivity::class.java)
                playerIntent.putExtra(AUDIO_PLAYER_DATA, it)
                startActivity(playerIntent)
            }
        }
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