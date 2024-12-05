package networking

import data.model.remote.UnsplashPhotoRemote
import data.model.remote.UnsplashResponseRemote
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query


internal interface ApiInterface {

    @Headers("Accept-Version: v1")
    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): UnsplashResponseRemote


    @GET("photos/{id}")
    suspend fun getPhoto(
        @Path("id") photoId: String,
    ): UnsplashPhotoRemote
}