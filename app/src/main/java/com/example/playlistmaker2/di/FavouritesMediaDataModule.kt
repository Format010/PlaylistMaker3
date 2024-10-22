import androidx.room.Room
import com.example.playlistmaker2.media.domain.FavouritesMediaRepository
import com.example.playlistmaker2.media.data.converter.TrackConverter
import com.example.playlistmaker2.media.data.db.AppDatabase
import com.example.playlistmaker2.media.data.impl.FavouritesMediaRepositoryImpl
import com.example.playlistmaker2.media.domain.FavouritesMediaInteractor
import com.example.playlistmaker2.media.domain.impl.FavouritesMediaInteractorImpl
import com.example.playlistmaker2.media.ui.favourites.MediaFavoriteTrackViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favouritesMediaDataModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }

    single<TrackConverter> {
        TrackConverter()
    }

    single<FavouritesMediaRepository> {
        FavouritesMediaRepositoryImpl(database = get(), converter =  get())
    }

    single<FavouritesMediaInteractor> {
        FavouritesMediaInteractorImpl(get())
    }
    viewModel {
        MediaFavoriteTrackViewModel(get())
    }

}



