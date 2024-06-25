package ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.github.ajalt.colormath.model.RGB


//light theme colors
val ColorWhite = Color(0xFFFFFFFF)

val String.color
    get() =  RGB(this).run {
        Color(red = this.r, green = this.g, blue = this.b, alpha = this.alpha)
    }
        

// dark theme colors
val ColorMatteBlack = Color(0xFF212121)


val appWhite
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) ColorWhite else ColorMatteBlack

val appDark
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) ColorMatteBlack else ColorWhite

val colorDisabledGray
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) ColorMatteBlack else Color(0xFFF5F5FD)

val colorGrayDivider
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) Color(0xFFF5F5FD) else Color(0xFFD4D8EB)

fun Color.complementary() = Color(
    red = 1F - red,
    green = 1F - green,
    blue = 1F - blue
)







