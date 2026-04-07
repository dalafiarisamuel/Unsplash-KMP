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
import platform.Photos.PHAccessLevelAddOnly
import platform.Photos.PHAssetChangeRequest
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusLimited
import platform.Photos.PHPhotoLibrary
import platform.UIKit.UIImage
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
                    return@withContext ImageDownloadState.Failure(Exception("No data was received"))
                }

                val image =
                    UIImage.imageWithData(data)
                        ?: return@withContext ImageDownloadState.Failure(
                            Exception("Invalid image data")
                        )

                saveImageToPhotos(image)
                ImageDownloadState.Success
            } catch (e: Exception) {
                ImageDownloadState.Failure(e)
            }
        }
    }

    private suspend fun fetchData(url: NSURL): Pair<NSData?, NSURLResponse?> =
        suspendCancellableCoroutine { continuation ->
            val session = NSURLSession.sharedSession
            val task =
                session.dataTaskWithURL(url) { data, response, error ->
                    if (error != null) {
                        continuation.resumeWithException(error.toKotlinException())
                    } else {
                        continuation.resume(Pair(data, response))
                    }
                }
            task.resume()
        }

    private suspend fun saveImageToPhotos(image: UIImage) =
        suspendCancellableCoroutine { continuation ->
            PHPhotoLibrary.requestAuthorizationForAccessLevel(PHAccessLevelAddOnly) { status ->
                when (status) {
                    PHAuthorizationStatusAuthorized,
                    PHAuthorizationStatusLimited -> {
                        PHPhotoLibrary.sharedPhotoLibrary()
                            .performChanges(
                                { PHAssetChangeRequest.creationRequestForAssetFromImage(image) },
                                completionHandler = { success, error ->
                                    if (success) {
                                        continuation.resume(Unit)
                                    } else {
                                        continuation.resumeWithException(
                                            error?.toKotlinException()
                                                ?: Exception("Failed to save image to photos")
                                        )
                                    }
                                },
                            )
                    }
                    else ->
                        continuation.resumeWithException(Exception("Photo library access denied"))
                }
            }
        }

    private fun NSError.toKotlinException(): Exception {
        return when (domain) {
            NSURLErrorDomain -> IOException(localizedDescription)
            else -> Exception(localizedDescription)
        }
    }
}
