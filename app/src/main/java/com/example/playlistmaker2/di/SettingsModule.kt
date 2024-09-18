import android.app.Application.MODE_PRIVATE
import com.example.playlistmaker2.SHARED_PREF_SETTINGS
import com.example.playlistmaker2.settings.data.SettingsRepository
import com.example.playlistmaker2.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker2.settings.domain.SettingsInteractor
import com.example.playlistmaker2.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker2.settings.ui.SettingsViewModel
import com.example.playlistmaker2.sharing.data.ExternalNavigator
import com.example.playlistmaker2.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker2.sharing.domain.SharingInteractor
import com.example.playlistmaker2.sharing.domain.impl.SharingInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    single {
        androidContext()
            .getSharedPreferences(SHARED_PREF_SETTINGS, MODE_PRIVATE)
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }



}