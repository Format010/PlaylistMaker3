package com.example.playlistmaker2.media.ui.playlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker2.AUDIO_PLAYER_DATA
import com.example.playlistmaker2.CLICK_DEBOUNCE_DELAY
import com.example.playlistmaker2.R
import com.example.playlistmaker2.databinding.FragmentPlaylistScreenBinding
import com.example.playlistmaker2.media.domain.model.Playlist
import com.example.playlistmaker2.player.ui.AudioPlayerActivity
import com.example.playlistmaker2.search.domain.model.Track
import com.example.playlistmaker2.search.ui.SearchAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistScreenFragment() : Fragment() {

    companion object {
        private var ID_PLAYLIST = 0
    }
    private val viewModel by viewModel<PlaylistScreenViewModel>()
    private var playlist:Playlist? = null
    private var isClickAllowed = true
    private lateinit var trackListAdapter: SearchAdapter
    private var summTrackTime: String = "0"
    private var dialog: MaterialAlertDialogBuilder? = null
    private var trackList = emptyList<Track>()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private var _binding: FragmentPlaylistScreenBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding wasn't initiliazed Screen!" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val item = arguments?.getInt("playlistId")
        item.let { item ->
            if (item == null){

            }else ID_PLAYLIST = item
        }

        _binding = FragmentPlaylistScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.stateLive.observe(viewLifecycleOwner){
            render(it)
        }

        viewModel.getNewPLaylist(ID_PLAYLIST)

        var constraintLayout = binding.constraintLayout
        val bottomSheet = binding.standardBottomSheet
        val behavior = BottomSheetBehavior.from(bottomSheet)
        var peekHeightBottomSheet : Int = 0
        constraintLayout.post {
            val constraintHeight = constraintLayout.height // Высота ConstraintLayout
            val displayHeight = resources.displayMetrics.heightPixels // Высота экрана
            peekHeightBottomSheet = displayHeight - constraintHeight - dpToPx(24F, view.context)
            behavior.peekHeight = peekHeightBottomSheet
        }

        binding.backAp.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val bottomSheetContainer = binding.menuBottomSheet

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset + 1
            }
        })

        binding.shareMenu.setOnClickListener {
            if (playlist?.trackCount == null || playlist?.trackCount!! < 1){
                Toast.makeText(context, R.string.empty_track_list_playlist , Toast.LENGTH_LONG).show()
            }else {
                shareAppLink(viewModel.sharePlaylistText())
            }
        }

        binding.share.setOnClickListener{
            if (playlist?.trackCount == null || playlist?.trackCount!! < 1){
                Toast.makeText(context, R.string.empty_track_list_playlist , Toast.LENGTH_LONG).show()
            }else {
                shareAppLink(viewModel.sharePlaylistText())
            }
        }

        binding.menu.setOnClickListener{
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.editInfoMenu.setOnClickListener {
            val fragment = EditorPlaylistFragment.newInstance().apply {
                arguments = Bundle().apply {
                    putInt("playlist", playlist?.playlistId!!)
                }
            }
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container_view, fragment)
                .addToBackStack(null)
                .commit()
        }

        binding.delPlaylist.setOnClickListener {
            showDialogDelPlaylist(playlist!!)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun initAllView() {
        binding.playlistName.text = playlist?.title
        binding.description.text = playlist?.description
        Glide.with(requireView())
            .load(playlist?.filePath)
            .placeholder(R.drawable.incorrect_link312)
            .transform(
                CenterCrop(), RoundedCorners(dpToPx(8f, requireContext()))
            )
            .into(binding.artwork)

        Glide.with(requireView())
            .load(playlist?.filePath)
            .placeholder(R.drawable.incorrect_link312)
            .transform(
                CenterCrop(), RoundedCorners(dpToPx(8f, requireContext()))
            )
            .into(binding.playlistImageMenu)

        binding.playlistNameMenu.text = playlist?.title

        trackListAdapter = SearchAdapter(trackList,
            { track ->
                if (clickDebounce()){
                    val playerIntent = Intent(requireContext(), AudioPlayerActivity::class.java)
                    playerIntent.putExtra(AUDIO_PLAYER_DATA, track)
                    startActivity(playerIntent)
                }
            }, {
                it.trackId?.let { it1 -> showDialogDelTrackInPlaylist(it1) }
            })
    }

    fun shareAppLink(shareText: String) {
        val shareApp = Intent()
        shareApp.action = Intent.ACTION_SEND
        shareApp.type = "text/plain"
        shareApp.putExtra(Intent.EXTRA_TEXT, shareText)
        shareApp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(shareApp)
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    private fun render(state: PlaylistScreenState) {
        when (state) {
            is PlaylistScreenState.Content -> showContent(state.trackList, state.playlist)
            is PlaylistScreenState.Loading -> showLoading()
            is PlaylistScreenState.Delite -> showDelite()
        }
    }

    private fun showContent(trackListContent: List<Track>, playlistChange: Playlist) {
        playlist = playlistChange
        initAllView()
        summTrackTime = SimpleDateFormat("mm", Locale.getDefault()).format(trackListContent.sumOf { it.trackTimeMillis?.toLongOrNull() ?: 0L })
        binding.timeAllTrack.text = context?.resources?.getQuantityString(R.plurals.time_of_tracks, summTrackTime.toInt(),summTrackTime.toInt())
        binding.progressBar.isVisible = false
        binding.playlistRecyclerView.isVisible = true
        trackList = trackListContent
        val trackCount = playlist?.trackCount ?: 0
        binding.numTracks.text = context?.resources?.getQuantityString(R.plurals.count_of_tracks, trackCount, trackCount)
        binding.countTrackMenu.text = context?.resources?.getQuantityString(R.plurals.count_of_tracks, trackCount, trackCount)
        binding.playlistRecyclerView.adapter = trackListAdapter

        requireView()

        if (trackListContent.isEmpty()){
            binding.playlistRecyclerView.isVisible = false
            binding.emptyPlaylistText.isVisible = true
        }else{
            binding.playlistRecyclerView.isVisible = true
            binding.emptyPlaylistText.isVisible = false
        }

        trackListAdapter.data = emptyList()
        trackListAdapter.data = trackListContent.reversed()
        trackListAdapter.notifyDataSetChanged()
    }

    private fun showDelite(){
        parentFragmentManager.popBackStack()
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
        binding.playlistRecyclerView.isVisible = false
    }

    private fun showDialogDelTrackInPlaylist(trackId: String) {
        dialog = MaterialAlertDialogBuilder(requireContext(), R.style.dialogStyle)
            .setTitle(R.string.screen_playlist_del_track_message)
            .setMessage(R.string.message_material_dialog_del_track)
            .setNegativeButton(R.string.negative_button_material_dialog_del_track){ dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.positive_button_material_dialog_del_track) { dialog, which ->
                viewModel.delTrackInPlaylist(trackId, playlist!!)
            }
        dialog?.show()
    }

    private fun showDialogDelPlaylist(playlist: Playlist) {
        val message = resources.getString(R.string.message_dialog_del_playlist)
        dialog = MaterialAlertDialogBuilder(requireContext(), R.style.dialogStyle)
            .setTitle(R.string.screen_playlist_del_playlist_message)
            .setMessage("$message «${playlist.title}»?")
            .setNegativeButton(R.string.negative_button_material_dialog_del_track){ dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.positive_button_material_dialog_del_track) { dialog, which ->
                viewModel.delPlayList(playlist)
            }
        dialog?.show()
    }

    override fun onResume() {
        super.onResume()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}