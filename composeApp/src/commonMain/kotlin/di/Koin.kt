package di

import data.mapper.PhotoCreatorMapper
import data.mapper.PhotoMapper
import data.mapper.PhotosUrlsMapper
import data.repository.ImageRepository
import data.repository.ImageRepositoryImpl
import data.repository.SharedRepository
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import networking.ApiInterface
import networking.createApiInterface
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import ui.viewmodel.HomeScreenViewModel
import ui.viewmodel.PhotoDetailViewModel

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            networkModule(),
            mapperModule(),
            repositoryModule(),
            viewModelModule()
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
    single { SharedRepository() }
}

private fun viewModelModule() = module {
    viewModelOf(::HomeScreenViewModel)
    viewModelOf(::PhotoDetailViewModel)
}