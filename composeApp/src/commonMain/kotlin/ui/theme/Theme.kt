package ui.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import ng.devtamuno.unsplash.compose.ui.theme.Shapes

private val DarkColorPalette = darkColors(
    primary = ColorWhite,
    primaryVariant = ColorWhite,
    secondary = ColorMatteBlack,
)

private val LightColorPalette = lightColors(
    primary = ColorMatteBlack,
    primaryVariant = ColorMatteBlack,
    secondary = ColorWhite,

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@ExperimentalFoundationApi
@Composable
fun UnsplashKMPTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit,
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