package networking

import data.model.remote.UnsplashPhotoRemote
import data.model.remote.UnsplashResponseRemote
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import env.Env


interface ApiInterface {

    @Headers("Accept-Version: v1")
    @GET("search/photos")
    suspend fun searchPhotos(
        @Header("Authorization") authorization: String = "Client-ID ${Env.API_KEY}",
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): UnsplashResponseRemote


    @GET("photos/{id}")
    suspend fun getPhoto(
        @Header("Authorization") authorization: String = "Client-ID ${Env.API_KEY}",
        @Path("id") photoId: String,
    ): UnsplashPhotoRemote
}