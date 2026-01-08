package ui.download

import io.ktor.client.HttpClient
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.Url
import io.ktor.utils.io.toByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ui.state.ImageDownloadState
import java.io.File
import java.nio.file.Paths

internal actual class PlatformDownloadImage(private val client: HttpClient) {
    actual suspend fun downloadImage(imageLink: String): ImageDownloadState {
        return try {
            val fileName = "${Url(imageLink).rawSegments[1]}.jpeg"

            val response =
                client.get(imageLink) {
                    onDownload { bytesSentTotal, contentLength ->
                        println("Downloaded $bytesSentTotal of $contentLength")
                    }
                }
            val body = response.bodyAsChannel()
            val result = body.toByteArray()
            saveImageToFile(result, fileName)
            ImageDownloadState.Success
        } catch (e: Exception) {
            ImageDownloadState.Failure(e)
        }
    }

    private suspend fun saveImageToFile(imageBytes: ByteArray, fileName: String) {
        withContext(Dispatchers.IO) {
            val userHome = System.getProperty("user.home")
            val desktopPath = Paths.get(userHome, "Downloads", fileName).toString()
            val file = File(desktopPath)
            file.writeBytes(imageBytes)
            println("Image saved to $desktopPath")
        }
    }
}
