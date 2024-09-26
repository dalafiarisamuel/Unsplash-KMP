package ui.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import unsplashkmp.composeapp.generated.resources.Res
import unsplashkmp.composeapp.generated.resources.poppins_bold
import unsplashkmp.composeapp.generated.resources.poppins_light
import unsplashkmp.composeapp.generated.resources.poppins_medium
import unsplashkmp.composeapp.generated.resources.poppins_regular
import unsplashkmp.composeapp.generated.resources.poppins_semi_bold


@Composable
internal fun getPoppinsFontFamily() = FontFamily(
    Font(Res.font.poppins_regular, FontWeight.Normal),
    Font(Res.font.poppins_medium, FontWeight.Medium),
    Font(Res.font.poppins_light, FontWeight.Light),
    Font(Res.font.poppins_semi_bold, FontWeight.SemiBold),
    Font(Res.font.poppins_bold, FontWeight.Bold)
)

// Set of Material typography styles to start with
@Composable
internal fun getTypography() = Typography(
    defaultFontFamily = getPoppinsFontFamily(),
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)


