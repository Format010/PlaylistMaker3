import android.content.Context
import android.net.ConnectivityManager
import com.example.playlistmaker2.USER_KEY_HISTORY
import com.example.playlistmaker2.search.data.HistoryRepository
import com.example.playlistmaker2.search.data.NetworkClient
import com.example.playlistmaker2.search.data.SearchRepository
import com.example.playlistmaker2.search.data.impl.HistoryRepositoryImpl
import com.example.playlistmaker2.search.data.impl.SearchRepositoryImpl
import com.example.playlistmaker2.search.data.network.AppleItunesApi
import com.example.playlistmaker2.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker2.search.domain.HistoryInteractor
import com.example.playlistmaker2.search.domain.SearchInteractor
import com.example.playlistmaker2.search.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker2.search.domain.impl.SearchInteractorImpl
import com.example.playlistmaker2.search.ui.SearchViewModel
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchModule = module {
    //Network
    single<AppleItunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AppleItunesApi::class.java)
    }

    single<ConnectivityManager> {
        androidContext().getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    single<NetworkClient> {
        RetrofitNetworkClient(connectivityManager = get(), imdbService =  get())
    }
//Search and History
    single<SearchInteractor> {
        SearchInteractorImpl(get())
    }

    single<HistoryInteractor> {
        HistoryInteractorImpl(get())
    }

    single<SearchRepository> {
        SearchRepositoryImpl(get())
    }

    single {
        androidContext()
            .getSharedPreferences(USER_KEY_HISTORY, Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<HistoryRepository> {
        HistoryRepositoryImpl(get(), get())
    }

    viewModel {
        SearchViewModel(get(), get(), get())
    }

}