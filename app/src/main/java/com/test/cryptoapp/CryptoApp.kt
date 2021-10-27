package com.test.cryptoapp

import android.app.Application
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.test.cryptoapp.data.repository.coins.*
import com.test.cryptoapp.data.repository.user.UserDataStore
import com.test.cryptoapp.data.repository.user.UserDataStoreImpl
import com.test.cryptoapp.data.repository.user.UserRepository
import com.test.cryptoapp.data.repository.user.UserRepositoryImpl
import com.test.cryptoapp.domain.db.AppDatabase
import com.test.cryptoapp.domain.db.DatabaseHelper
import com.test.cryptoapp.domain.db.DatabaseHelperImpl
import com.test.cryptoapp.domain.net.Api
import com.test.cryptoapp.ui.fragments.coinslist.FragmentCoinsListViewModel
import com.test.cryptoapp.ui.fragments.consdetails.FragmentCoinDetailsViewModel
import com.test.cryptoapp.ui.fragments.settings.FragmentSettingsViewModel
import com.test.cryptoapp.ui.fragments.splashScreen.FragmentSplashScreenViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CryptoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CryptoApp)
            androidLogger(org.koin.core.logger.Level.DEBUG)
            modules(
                listOf(
                    splashScreenViewModel,
                    settingsViewModel,
                    coinsListViewModel,
                    coinsDetailsViewModel,
                    storeUserModule,
                    storeRemoteCoinModule,
                    repositoryUserModule,
                    apiModule,
                    databaseModule,
                    storeLocalCoinModule,
                    repositoryCoinModule,
                    databaseHelperModule,
                    coroutineDispatcherModule
                )
            )
        }
    }

}

//TODO !REFACTORING! Better to use one module in separate class for all viewModels, repositories, api etc. For example:
// val splashScreenViewModel = module {
//    viewModel { FragmentSplashScreenViewModel(get()) }
//    viewModel { FragmentSettingsViewModel(get()) }
// }
val splashScreenViewModel = module {
    viewModel {
        FragmentSplashScreenViewModel(get())
    }
}

val settingsViewModel = module {
    viewModel {
        FragmentSettingsViewModel(get())
    }
}

val coinsListViewModel = module {
    viewModel {
        FragmentCoinsListViewModel(get())
    }
}

val coinsDetailsViewModel = module {
    viewModel {
        FragmentCoinDetailsViewModel(get(), get())
    }
}

val apiModule = module {
    fun provideApi(): Api {
        val builder = GsonBuilder()

        val gson = builder.create()
        val logging = HttpLoggingInterceptor()

        // set your desired log level
        val httpClient = OkHttpClient.Builder()

        return Retrofit.Builder()
            .baseUrl("https://api.coingecko.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(
                httpClient.addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build()
            )
            .build()
            .create(Api::class.java)
    }

    single { provideApi() }
}

val databaseModule = module {
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "database")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    single { provideDatabase(androidApplication()) }
}

val databaseHelperModule = module {
    fun provideHelperDatabase(appDatabase: AppDatabase): DatabaseHelper {
        return DatabaseHelperImpl(appDatabase)
    }

    single { provideHelperDatabase(get()) }
}

val storeUserModule = module {
    fun provideUserDataStore(dbHelper: DatabaseHelper): UserDataStore {
        return UserDataStoreImpl(dbHelper)
    }

    single { provideUserDataStore(get()) }
}

val storeRemoteCoinModule = module {

    fun provideRemoteCoinDataStore(api: Api): RemoteCoinsDataStore {
        return RemoteCoinsDataStoreImpl(api)
    }

    single { provideRemoteCoinDataStore(get()) }
}

val storeLocalCoinModule = module {
    fun provideUserDataStore(dbHelper: DatabaseHelper): LocalCoinsDataStore {
        return LocalCoinsDataStoreImpl(dbHelper)
    }

    single { provideUserDataStore(get()) }
}

val repositoryCoinModule = module {
    fun provideCoinRepository(
        localCoinsDataStore: LocalCoinsDataStore,
        remoteCoinsDataStore: RemoteCoinsDataStore
    ): CoinsRepository {
        return CoinsRepositoryImpl(localCoinsDataStore, remoteCoinsDataStore)
    }

    single { provideCoinRepository(get(), get()) }
}

val repositoryUserModule = module {
    fun provideUserRepository(userDataStore: UserDataStore): UserRepository {
        return UserRepositoryImpl(userDataStore)
    }

    single { provideUserRepository(get()) }
}

val coroutineDispatcherModule = module {
    fun provideCoroutineDispatcher() : CoroutineDispatcher {
        return Dispatchers.IO
    }

    single { provideCoroutineDispatcher() }
}