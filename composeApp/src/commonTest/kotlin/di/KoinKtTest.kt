package di

import data.mapper.PhotoCreatorMapper
import data.mapper.PhotoMapper
import data.mapper.PhotosUrlsMapper
import kotlinx.serialization.json.Json
import org.koin.dsl.module


fun networkModule() = module {
    single {
        Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        }
    }
}

fun mapperModule() = module {
    single { PhotoCreatorMapper() }
    single { PhotosUrlsMapper() }
    single { PhotoMapper(get(), get()) }
}