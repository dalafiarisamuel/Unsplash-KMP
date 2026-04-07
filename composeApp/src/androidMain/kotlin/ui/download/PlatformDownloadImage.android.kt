package ui.download

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ui.state.ImageDownloadState

internal actual class PlatformDownloadImage(private val context: Context) {
    actual suspend fun downloadImage(imageLink: String): ImageDownloadState {
        return withContext(Dispatchers.IO) {
            try {
                val uri = imageLink.toUri()
                val ext =
                    MimeTypeMap.getFileExtensionFromUrl(imageLink).takeIf { it.isNotEmpty() }
                        ?: uri.getQueryParameter("fm")
                        ?: "jpg"
                val baseName =
                    (uri.lastPathSegment ?: uri.hashCode().toString()).substringBeforeLast('.')
                val fileName = "$baseName.$ext"

                val request =
                    DownloadManager.Request(uri)
                        .setNotificationVisibility(
                            DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
                        )
                        .setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_DOWNLOADS,
                            fileName,
                        )

                (context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).enqueue(
                    request
                )
                ImageDownloadState.Success
            } catch (e: Exception) {
                ImageDownloadState.Failure(e)
            }
        }
    }
}
