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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {

    private lateinit var placeholderMessage: ViewGroup
    private lateinit var placeholderMessage2: ViewGroup
    private lateinit var inputEditText: EditText
    private var textValue: String = TEXT
    private val appleItunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(appleItunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val appleItunesApi = retrofit.create(AppleItunesApi::class.java)
    val listSong = ArrayList<Track>()
    val songAdapter = SearchAdapter(listSong)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val back = findViewById<Button>(R.id.back_search)
        inputEditText = findViewById<EditText>(R.id.input_search)
        val clearButton = findViewById<ImageView>(R.id.clear_text)
        val update = findViewById<Button>(R.id.update)
        placeholderMessage = findViewById(R.id.placeholder_message)
        placeholderMessage2 = findViewById(R.id.placeholder_message2)



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
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
                listSong.clear()
                songAdapter.notifyDataSetChanged()
            }
        }

        update.setOnClickListener {
            searchMusicFun()
            if (placeholderMessage2.visibility == View.VISIBLE) {
                placeholderMessage2.visibility = View.GONE
            }

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
            }

            override fun afterTextChanged(s: Editable?) {
                textValue = s.toString()
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        val rvSearch = findViewById<RecyclerView>(R.id.searchRecyclerView)
        rvSearch.adapter = songAdapter

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDITED_TEXT, textValue)
    }

    private companion object {
        const val EDITED_TEXT = "KEY"
        const val TEXT = " "
    }

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

    fun showMessage(text: String, additionalMessage: String) {
        when (text) {
            "Ничего не нашлось" -> {
                if (text.isNotEmpty()) {
                    listSong.clear()
                    placeholderMessage.visibility = View.VISIBLE
                    if (additionalMessage.isNotEmpty()) {
                        Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }

            "Проблемы со связью" -> {
                if (text.isNotEmpty()) {
                    listSong.clear()
                    placeholderMessage2.visibility = View.VISIBLE
                    if (additionalMessage.isNotEmpty()) {
                        Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }


}
