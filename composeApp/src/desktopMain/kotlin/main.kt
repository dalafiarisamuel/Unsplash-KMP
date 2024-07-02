import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import di.initKoin
import java.awt.Dimension

fun main() {
    initKoin()

    application {

        val windowState = rememberWindowState(
            size = DpSize(width = 900.dp, height = 950.dp),
            position = WindowPosition(Alignment.Center)
        )

        Window(
            onCloseRequest = ::exitApplication,
            title = "UnsplashKMP",
            state = windowState
        ) {
            window.minimumSize = Dimension(950, 900)
            App()
        }
    }
}