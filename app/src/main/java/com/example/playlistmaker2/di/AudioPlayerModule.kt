
import android.media.MediaPlayer
import com.example.playlistmaker2.player.data.AudioPlayerRepository
import com.example.playlistmaker2.player.data.impl.AudioPlayerRepositoryImpl
import com.example.playlistmaker2.player.domain.AudioPlayerInteractor
import com.example.playlistmaker2.player.domain.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker2.player.ui.AudioPlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val audioPlayerModule = module {

    single<AudioPlayerInteractor> {
        AudioPlayerInteractorImpl(get())
    }

    factory<MediaPlayer?> {
        MediaPlayer()
    }

    single<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(get())
    }

    viewModel { (url: String, idTrack: String) ->
        AudioPlayerViewModel(get(), get(), url, idTrack)
    }



}