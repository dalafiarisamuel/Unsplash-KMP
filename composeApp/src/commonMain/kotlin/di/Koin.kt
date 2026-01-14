package di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import data.local.database.DatabaseFactory
import data.local.database.UnsplashPhotoDatabase
import data.local.preference.SettingsPreferenceFactory
import data.local.repository.UnsplashImageLocalRepository
import data.mapper.PhotoCreatorMapper
import data.mapper.PhotoMapper
import data.mapper.PhotosUrlsMapper
import data.remote.repository.ImageRepository
import data.repository.ImageRepositoryImpl
import data.repository.PreferenceRepositoryImpl
import data.repository.UnsplashImageLocalRepositoryImpl
import data.ui.repository.PreferenceRepository
import de.jensklingenberg.ktorfit.Ktorfit
import domain.usecase.ClearAllPhotosUseCase
import domain.usecase.DeletePhotoUseCase
import domain.usecase.GetAllPhotoAsFlowUseCase
import domain.usecase.GetPhotoByIdUseCase
import domain.usecase.SavePhotoUseCase
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.serialization.json.Json
import networking.ApiInterface
import networking.AuthorizationTokenInterceptor
import networking.createApiInterface
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module
import ui.viewmodel.BookmarkScreenViewModel
import ui.viewmodel.HomeScreenViewModel
import ui.viewmodel.PhotoDetailViewModel
import ui.viewmodel.SharedViewModel

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(
        networkModule(),
        mapperModule(),
        repositoryModule(),
        viewModelModule(),
        platformModule(),
        sharedModule(),
        useCaseModule(),
    )
}

// called by iOS etc
fun initKoin() = initKoin() {}

private fun networkModule() = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    }
                )
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }

            install(AuthorizationTokenInterceptor) {
                tokenProvider = { "" }
            }
        }
    }

    single<Ktorfit> {
        Ktorfit.Builder().build {
            httpClient(get<HttpClient>())
            baseUrl("https://api.unsplash.com/")
        }
    }
    single<ApiInterface> { get<Ktorfit>().createApiInterface() }
}

private fun mapperModule() = module {
    single { PhotoCreatorMapper() }
    single { PhotosUrlsMapper() }
    single { PhotoMapper(get(), get()) }
}

private fun repositoryModule() = module {
    single<ImageRepository> { ImageRepositoryImpl(get()) }
    single<PreferenceRepository> { PreferenceRepositoryImpl(get()) }
    singleOf(::UnsplashImageLocalRepositoryImpl).bind<UnsplashImageLocalRepository>()
}

private fun useCaseModule() = module {
    singleOf(::ClearAllPhotosUseCase)
    singleOf(::DeletePhotoUseCase)
    singleOf(::GetAllPhotoAsFlowUseCase)
    singleOf(::GetPhotoByIdUseCase)
    singleOf(::SavePhotoUseCase)
}

private fun viewModelModule() = module {
    viewModelOf(::HomeScreenViewModel)
    viewModelOf(::PhotoDetailViewModel)
    viewModelOf(::BookmarkScreenViewModel)
    viewModelOf(::SharedViewModel)
}

private fun sharedModule() = module {
    single { get<UnsplashPhotoDatabase>().getPhotoDao() }
    single {
        get<DatabaseFactory>()
            .create()
            .fallbackToDestructiveMigration(dropAllTables = true)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
    single<DataStore<Preferences>> { get<SettingsPreferenceFactory>().createDataStore() }
}
