package ui.download

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okio.IOException
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Foundation.NSURL
import platform.Foundation.NSURLErrorDomain
import platform.Foundation.NSURLResponse
import platform.Foundation.NSURLSession
import platform.Foundation.dataTaskWithURL
import platform.UIKit.UIImage
import platform.UIKit.UIImageWriteToSavedPhotosAlbum
import ui.state.ImageDownloadState
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalForeignApi::class)
internal actual class PlatformDownloadImage {

    actual suspend fun downloadImage(imageLink: String): ImageDownloadState {
        return withContext(Dispatchers.IO) {
            try {
                val nsUrl = NSURL(string = imageLink)

                val (data, _) = fetchData(nsUrl)

                if (data == null) {
                    println("No data received")
                    return@withContext ImageDownloadState.Failure(Exception("No data was received"))
                }

                val image = UIImage(data = data)

                saveImageToPhotos(image)
                println("Image saved successfully")
                ImageDownloadState.Success
            } catch (e: Exception) {
                ImageDownloadState.Failure(e)
            }
        }
    }

    private suspend fun fetchData(url: NSURL): Pair<NSData?, NSURLResponse?> =
        suspendCancellableCoroutine { continuation ->
            val session = NSURLSession.sharedSession
            val task = session.dataTaskWithURL(url) { data, response, error ->
                if (error != null) {
                    continuation.resumeWithException(error.toKotlinException())
                } else {
                    continuation.resume(Pair(data, response))
                }
            }
            task.resume()
        }

    private suspend fun saveImageToPhotos(image: UIImage) =
        suspendCancellableCoroutine<Unit> { continuation ->
            UIImageWriteToSavedPhotosAlbum(image, null, null, null)
            continuation.resume(Unit)
        }

    private fun NSError.toKotlinException(): Exception {
        return when (domain) {
            NSURLErrorDomain -> IOException(localizedDescription)
            else -> Exception(localizedDescription)
        }
    }
}