import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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

//    val MIGRATION_1_2 = object : Migration(1, 2) {
//        override fun migrate(database: SupportSQLiteDatabase) {
//            database.execSQL("ALTER TABLE favourites_media_table ADD COLUMN dataSort TEXT NOT NULL DEFAULT 0")
//        }
//    }


    single<AppDatabase> {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            //.addMigrations(MIGRATION_1_2)
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



