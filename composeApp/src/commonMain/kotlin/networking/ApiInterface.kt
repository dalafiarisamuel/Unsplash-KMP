package networking

import data.remote.model.UnsplashPhotoRemote
import data.remote.model.UnsplashResponseRemote
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
        @Query("order_by") orderBy: String = "latest",
    ): UnsplashResponseRemote


    @GET("photos/{id}")
    suspend fun getPhoto(
        @Path("id") photoId: String,
    ): UnsplashPhotoRemote
}
