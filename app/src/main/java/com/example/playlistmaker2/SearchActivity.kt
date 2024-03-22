package com.example.playlistmaker2

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.LinkedList

class SearchActivity : AppCompatActivity() {

    private lateinit var placeholderMessage: ViewGroup
    private lateinit var placeholderMessage2: ViewGroup
    private lateinit var inputEditText: EditText
    private lateinit var historyLayout: LinearLayout
    private var textValue: String = TEXT
    private val appleItunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(appleItunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val appleItunesApi = retrofit.create(AppleItunesApi::class.java)
    val listSong = LinkedList<Track>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val back = findViewById<Button>(R.id.back_search)
        val clearButton = findViewById<ImageView>(R.id.clear_text)
        val update = findViewById<Button>(R.id.update)
        inputEditText = findViewById(R.id.input_search)
        placeholderMessage = findViewById(R.id.placeholder_message)
        placeholderMessage2 = findViewById(R.id.placeholder_message2)
        historyLayout = findViewById(R.id.history_layout)
        val sharedPrefs = getSharedPreferences(NAME_HISTORY_FILE_PREFERENCE, MODE_PRIVATE)
        val saveHistory = SearchHistory(sharedPrefs)
        val songAdapter = SearchAdapter(listSong, saveHistory)
        val clearHistoryButton = findViewById<Button>(R.id.clear_history)
        val rvHistory = findViewById<RecyclerView>(R.id.historyRecyclerView)
        val rvSearch = findViewById<RecyclerView>(R.id.searchRecyclerView)


        fun searchMusicFun() {

            if (inputEditText.text.isNotEmpty()) {
                appleItunesApi.searchSong(inputEditText.text.toString())
                    .enqueue(object : Callback<MusicResponse> {
                        override fun onResponse(
                            call: Call<MusicResponse>,
                            response: Response<MusicResponse>
                        ) {
                            if (response.code() == 200) {
                                if (response.body()?.results?.isNotEmpty() == true) {
                                    listSong.addAll(response.body()?.results!!)
                                    songAdapter.notifyDataSetChanged()
                                }
                                if (listSong.isEmpty()) {
                                    showMessage("Ничего не нашлось", "")
                                } else {
                                    showMessage("", "")
                                }
                            } else {
                                showMessage("Проблемы со связью", response.code().toString())
                            }
                        }

                        override fun onFailure(call: Call<MusicResponse>, t: Throwable) {
                            showMessage("Проблемы со связью", t.message.toString())
                        }
                    })
            }
        }

        if (savedInstanceState != null) {
            textValue = savedInstanceState.getString(EDITED_TEXT, TEXT)
            inputEditText.setText(textValue)
        }

        back.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val view: View? = this.currentFocus
            if (view != null) {
                val inputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                listSong.clear()
                songAdapter.notifyDataSetChanged()
                rvHistory.adapter = HistoryAdapter(saveHistory.read())
                if (placeholderMessage2.visibility == View.VISIBLE) {
                    placeholderMessage2.visibility = View.GONE
                }
                if (placeholderMessage.visibility == View.VISIBLE) {
                    placeholderMessage.visibility = View.GONE
                }

            }
        }

        update.setOnClickListener {
            searchMusicFun()
            if (placeholderMessage2.visibility == View.VISIBLE) {
                placeholderMessage2.visibility = View.GONE
            }
        }

        clearHistoryButton.setOnClickListener{
            saveHistory.clearSearch()
            rvHistory.adapter = HistoryAdapter(saveHistory.read())
            historyLayout.visibility = View.GONE
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (placeholderMessage2.visibility == View.VISIBLE) {
                    placeholderMessage2.visibility = View.GONE
                }
                if (placeholderMessage.visibility == View.VISIBLE) {
                    placeholderMessage.visibility = View.GONE
                }
                listSong.clear()
                searchMusicFun()
            }
            false
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
                historyLayout.visibility = if (inputEditText.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
                historyLayout.visibility = if(saveHistory.read().isEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                textValue = s.toString()
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        rvSearch.adapter = songAdapter
        rvHistory.adapter = HistoryAdapter(saveHistory.read())
        if (saveHistory.read().isNotEmpty()) historyLayout.visibility = View.VISIBLE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDITED_TEXT, textValue)
    }

    private companion object {
        const val EDITED_TEXT = "KEY"
        const val TEXT = " "
    }

    fun showMessage(text: String, additionalMessage: String) {
        when (text) {
            "Ничего не нашлось" -> {
                if (text.isNotEmpty()) {
                    listSong.clear()
                    placeholderMessage.visibility = View.VISIBLE

                }
            }

            "Проблемы со связью" -> {
                if (text.isNotEmpty()) {
                    listSong.clear()
                    placeholderMessage2.visibility = View.VISIBLE

                }
            }
        }
    }
}
