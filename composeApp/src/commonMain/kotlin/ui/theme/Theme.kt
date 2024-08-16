package ui.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ng.devtamuno.unsplash.compose.ui.theme.Shapes

private val DarkColorPalette = darkColors(
    primary = ColorWhite,
    primaryVariant = ColorWhite,
    secondary = ColorMatteBlack,
    background = ColorMatteBlack,
    onSecondary = ColorMatteBlack,
    secondaryVariant = Color(0xFFF5F5FD)
)

private val LightColorPalette = lightColors(
    primary = ColorMatteBlack,
    primaryVariant = ColorMatteBlack,
    secondary = ColorWhite,
    background = ColorWhite,
    onSecondary = Color(0xFFF5F5FD),
    secondaryVariant = Color(0xFFD4D8EB)
)

@ExperimentalFoundationApi
@Composable
fun UnsplashKMPTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = getTypography(),
        shapes = Shapes,
        content = content
    )
}