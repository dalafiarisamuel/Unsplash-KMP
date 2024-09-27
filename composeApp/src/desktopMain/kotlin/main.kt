import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import di.initKoin
import java.awt.Dimension
import org.jetbrains.compose.resources.painterResource
import unsplashkmp.composeapp.generated.resources.Res
import unsplashkmp.composeapp.generated.resources.icon

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
            icon = painterResource(Res.drawable.icon),
            state = windowState
        ) {
            window.minimumSize = Dimension(950, 900)
            App()
        }
    }
}