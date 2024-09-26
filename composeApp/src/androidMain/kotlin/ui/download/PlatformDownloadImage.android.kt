package ui.download

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ui.state.ImageDownloadState
import kotlin.time.Duration.Companion.seconds

internal actual class PlatformDownloadImage(private val context: Context) {
    actual suspend fun downloadImage(imageLink: String): ImageDownloadState {
        withContext(Dispatchers.Default) {
            delay(3.seconds)
        }
        return withContext(Dispatchers.Main) {
            try {
                val uri = Uri.parse(imageLink)
                val fileName = "${uri.pathSegments[0]}.jpeg"

                val request = DownloadManager.Request(uri)
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

                (context.getSystemService(Context.DOWNLOAD_SERVICE)
                        as DownloadManager).enqueue(request)
                ImageDownloadState.Success
            } catch (e: Exception) {
                ImageDownloadState.Failure(e)
            }
        }
    }
}