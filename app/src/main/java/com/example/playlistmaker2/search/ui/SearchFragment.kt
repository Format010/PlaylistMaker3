package com.example.playlistmaker2.search.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity.INPUT_METHOD_SERVICE
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker2.AUDIO_PLAYER_DATA
import com.example.playlistmaker2.CLICK_DEBOUNCE_DELAY
import com.example.playlistmaker2.EDITED_TEXT
import com.example.playlistmaker2.databinding.FragmentSearchBinding
import com.example.playlistmaker2.player.ui.AudioPlayerActivity
import com.example.playlistmaker2.search.domain.model.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    companion object {
        private const val TEXT = ""
    }

    private lateinit var binding: FragmentSearchBinding
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
    private var messageNothingWasFound: ViewGroup? = null
    private var messageConnectError: ViewGroup? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            render(it)
        }

        messageNothingWasFound = binding.nothingWasFound
        messageConnectError = binding.communicationProblems
        historyLayout = binding.historyLayout
        inputEditText = binding.inputSearch
        progressBar = binding.progressBar
        clearButton = binding.clearText
        rvSearch = binding.searchRecyclerView
        rvHistory = binding.historyRecyclerView

        init() //Инициализация добавления трека в историю и переход в плеер

        rvSearch.adapter = songAdapter
        rvHistory.adapter = historyAdapter
        val update = binding.update
        val clearHistoryButton = binding.clearHistory


        viewModel.historyList.observe(viewLifecycleOwner){
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
            if (messageConnectError?.isVisible == true) {
                messageConnectError?.isVisible = false
            }
            if (messageNothingWasFound?.isVisible == true) {
                messageNothingWasFound?.isVisible = false
            }
            viewModel.searchDebounce(changedText = s?.toString() ?: "")

            clearButton.isVisible = !s.isNullOrEmpty()
            historyLayout.isVisible = viewModel.historyRead().isNotEmpty()

            if (inputEditText.hasFocus() && s?.isEmpty() == true){
                historyLayout.isVisible = true
                songAdapter.data = emptyList()
                songAdapter.notifyDataSetChanged()
                historyAdapter.data = viewModel.historyRead()
            }
        },
            afterTextChanged = { a: Editable? ->
                textValue = a.toString()

            })

        clearButton.setOnClickListener {
            inputEditText.setText(TEXT)
            if (view != null) {
                val inputMethodManager =
                    requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                songAdapter.data = emptyList()
                songAdapter.notifyDataSetChanged()
                historyAdapter.data = viewModel.historyRead()
                historyAdapter.notifyDataSetChanged()
                if (messageConnectError?.isVisible == true) {
                    messageConnectError?.isVisible = false
                }
                if (messageNothingWasFound?.isVisible == true) {
                    messageNothingWasFound?.isVisible = false
                }
            }
        }

        update.setOnClickListener {
            viewModel.searchMusicFun(inputEditText.text.toString())
            songAdapter.notifyDataSetChanged()
            if (messageConnectError?.isVisible == true) {
                messageConnectError?.isVisible = false
            }
        }

        clearHistoryButton.setOnClickListener {
            viewModel.historyClear()
            historyAdapter.data = viewModel.historyRead()
            historyLayout.isVisible = false
        }


    }

    private fun showLoading() {
        messageConnectError?.visibility = View.GONE
        messageNothingWasFound?.visibility = View.GONE
        historyLayout.visibility = View.GONE
        rvSearch.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun showEmpty() {
        progressBar.visibility = View.GONE
        messageConnectError?.visibility = View.GONE
        messageNothingWasFound?.visibility = View.VISIBLE
    }

    private fun showError() {
        progressBar.visibility = View.GONE
        messageConnectError?.visibility = View.VISIBLE
    }

    private fun showContent(song: List<Track>) {
        progressBar.visibility = View.GONE
        messageNothingWasFound?.visibility = View.GONE
        messageConnectError?.visibility = View.GONE
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
                val playerIntent = Intent(requireContext(), AudioPlayerActivity::class.java)
                playerIntent.putExtra(AUDIO_PLAYER_DATA, it)
                startActivity(playerIntent)
            }
        }

        songAdapter = SearchAdapter(listSong){
            if (clickDebounce()) {
                viewModel.historyAdd(viewModel.historyRead(), it)
                val playerIntent = Intent(requireContext(), AudioPlayerActivity::class.java)
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