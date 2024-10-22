
import com.example.playlistmaker2.media.presentation.MediaPlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaModule = module {

    viewModel {
        MediaPlaylistViewModel()
    }
}