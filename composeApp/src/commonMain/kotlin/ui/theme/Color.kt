package ui.theme

import androidx.compose.material.MaterialTheme
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
    get() = MaterialTheme.colors.primary

val appDark
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.colors.secondary

val colorDisabledGray
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.colors.onSecondary

val colorGrayDivider
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.colors.secondaryVariant

fun Color.complementary() = Color(
    red = 1F - red,
    green = 1F - green,
    blue = 1F - blue
)







