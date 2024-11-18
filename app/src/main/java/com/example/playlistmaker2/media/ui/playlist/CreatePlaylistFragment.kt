package com.example.playlistmaker2.media.ui.playlist

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.playlistmaker2.R
import com.example.playlistmaker2.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker2.media.domain.model.Playlist
import com.example.playlistmaker2.player.ui.audioPlayerToFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class CreatePlaylistFragment : Fragment() {

    companion object {
        private var PREVIOUS_SCREEN = "previous screen"
        fun newInstance(previewScreen: String): CreatePlaylistFragment {
            val fragment = CreatePlaylistFragment()
            val args = Bundle().apply {
                PREVIOUS_SCREEN = previewScreen
            }
            fragment.arguments = args
            return fragment
        }
    }

    private var interactorAP: audioPlayerToFragment? = null
    lateinit var pickMedia : ActivityResultLauncher<PickVisualMediaRequest>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is audioPlayerToFragment) {
            interactorAP = context
        } else {
            Log.d("Context:", "$context must implement")
        }
    }

    private val viewModel by viewModel<CreatePlaylistViewModel>()
    private var newUri: Uri? = null
    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding wasn't initiliazed!" }
    var dialog: MaterialAlertDialogBuilder? = null
    private val requester = PermissionRequester.instance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val item = arguments?.getString("playlist")
        item.let { item ->
            if (item.isNullOrEmpty()) {
            } else PREVIOUS_SCREEN = item
        }

        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root

    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showDialog()

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
                        .into(binding.artwork)
                } else {
                    Log.d("PhotoPicker", "Ничего не выбрано")
                }
            }

        binding.artwork.setOnClickListener {
            when(Build.VERSION.SDK_INT){
                in 1..32 -> permissionMedia(Manifest.permission.READ_EXTERNAL_STORAGE)
                    else -> permissionMedia(Manifest.permission.READ_MEDIA_IMAGES)
            }

        }

        binding.titleText.addTextChangedListener(onTextChanged = { s: CharSequence?, _, _, _ ->
                binding.createBtn.isEnabled = s?.isEmpty() != true
        })

        binding.backPlaylist.setOnClickListener {
            if(newUri != null || binding.titleText.text?.isEmpty() != true || binding.descriptionText.text?.isEmpty() != true) {
                dialog?.show()
            }else {
                if (PREVIOUS_SCREEN == "AudioPlayer") {
                    interactorAP?.showElementUi()
                } else parentFragmentManager.popBackStack()
            }
        }

        binding.createBtn.setOnClickListener {
            viewModel.addNewPlaylistToDb(createNewPlaylist())
            if (PREVIOUS_SCREEN == "AudioPlayer") {
                interactorAP?.showElementUi()
            } else parentFragmentManager.popBackStack()

        }

        activity?.onBackPressedDispatcher?.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(newUri != null || binding.titleText.text?.isEmpty() != true || binding.descriptionText.text?.isEmpty() != true) {
                    dialog?.show()
                }else {
                    if (PREVIOUS_SCREEN == "AudioPlayer") {
                        interactorAP?.showElementUi()
                    } else parentFragmentManager.popBackStack()
                }
            }
        })
    }

    private fun createNewPlaylist(): Playlist {
        val title = binding.titleText.text.toString()
        val description = binding.descriptionText.text.toString()
        val filePath = newUri
        return Playlist(
            playlistId = 0,
            title = title,
            description = description,
            filePath = filePath.toString(),
            trackId = null,
            trackCount = null
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

    private fun showDialog() {
            dialog = MaterialAlertDialogBuilder(requireContext(), R.style.dialogStyle)
                .setTitle(R.string.message_end_playlist) // Заголовок диалога
                .setMessage(R.string.message_lost_data_playlist) // Описание диалога
                .setNeutralButton(R.string.cancel) { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton(R.string.complete) { dialog, which ->
                    if (PREVIOUS_SCREEN == "AudioPlayer") {
                        interactorAP?.showElementUi()
                    } else parentFragmentManager.popBackStack()
                }
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

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}