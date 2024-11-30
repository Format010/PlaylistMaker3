package com.example.playlistmaker2.media.ui.playlist

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker2.R
import com.example.playlistmaker2.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker2.media.domain.model.Playlist
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class EditorPlaylistFragment: CreatePlaylistFragment() {
    companion object {
        private var IDPLAYLIST = 0
        fun newInstance() = EditorPlaylistFragment()

    }

    private val viewModel by viewModel<CreatePlaylistViewModel>()
    private var newUri: Uri? = null
    private val requester = PermissionRequester.instance()
    private var playlist: Playlist? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item = arguments?.getInt("playlist")
        item.let { item ->
            IDPLAYLIST = item ?: 0
        }



        viewModel.playlistLive.observe(viewLifecycleOwner){
            render(it)
        }
        viewModel.getDataPlaylist(IDPLAYLIST)

        binding.backPlaylist.text = getString(R.string.text_titel_edit_playlist)
        binding.createBtn.text = getString(R.string.text_btn_edit_playlist)

        pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    newUri = saveImageToPrivateStorage(uri)
                    Glide.with(this)
                        .load(newUri)
                        .apply(
                            RequestOptions().transform(
                                MultiTransformation(
                                    CenterCrop(), RoundedCorners(
                                        dpToPx(8f, requireContext())
                                    )
                                )
                            )
                        )
                        .into(binding.artworkCreate)
                } else {
                    Log.d("PhotoPicker", "Ничего не выбрано")
                }
            }

        binding.artworkCreate.setOnClickListener {
            when(Build.VERSION.SDK_INT){
                in 1..32 -> permissionMedia(Manifest.permission.READ_EXTERNAL_STORAGE)
                else -> permissionMedia(Manifest.permission.READ_MEDIA_IMAGES)
            }

        }

        binding.titleText.addTextChangedListener(onTextChanged = { s: CharSequence?, _, _, _ ->
            binding.createBtn.isEnabled = s?.isEmpty() != true
        })

        binding.backPlaylist.setOnClickListener {
             parentFragmentManager.popBackStack()
        }

        binding.createBtn.setOnClickListener {
            viewModel.updatePlaylist(createNewPlaylist())

            parentFragmentManager.popBackStack()

        }

        activity?.onBackPressedDispatcher?.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.popBackStack()
            }
        })


    }

    private fun createNewPlaylist(): Playlist {
        val title = binding.titleText.text.toString()
        val description = binding.descriptionText.text.toString()
        val filePath = newUri
        return Playlist(
            playlistId = playlist?.playlistId ?: 0,
            title = title,
            description = description,
            filePath = filePath.toString(),
            trackId = playlist?.trackId,
            trackCount = playlist?.trackCount
        )
    }

    private fun saveImageToPrivateStorage(uri: Uri): Uri {

        val filePath =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "mypictures")

        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "${uri.pathSegments.last()}")
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return file.toUri()
    }

    private fun permissionMedia(manifest : String) {
        lifecycleScope.launch {
            requester.request(manifest).collect { resultReading ->
                when (resultReading) {
                    is PermissionResult.Granted -> {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                    is PermissionResult.Denied.NeedsRationale -> {
                        Toast.makeText(
                            requireContext(),
                            R.string.permission,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    is PermissionResult.Denied.DeniedPermanently -> {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.data = Uri.fromParts("package", context?.packageName, null)
                        context?.startActivity(intent)
                    }
                    PermissionResult.Cancelled -> {
                        return@collect
                    }
                }
            }
        }
    }

    fun render(playlist: Playlist){
        this.playlist = playlist

        Glide.with(this)
            .load(playlist.filePath?.toUri())
            .placeholder(R.drawable.incorrect_link312)
            .apply(
                RequestOptions().transform(
                    MultiTransformation(
                        CenterCrop(), RoundedCorners(
                            dpToPx(8f, requireContext())
                        )
                    )
                )
            )
            .into(binding.artworkCreate)

        binding.titleText.setText(playlist.title)
        binding.descriptionText.setText(playlist.description)
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}