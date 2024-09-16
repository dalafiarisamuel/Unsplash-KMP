package ui.theme

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat

@Composable
actual fun PlatformAppearance(isDarkTheme: Boolean) {
    val view = LocalView.current
    LaunchedEffect(isDarkTheme) {
        val window = (view.context as Activity).window
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = isDarkTheme.not()
            isAppearanceLightNavigationBars = isDarkTheme.not()
        }
    }
}