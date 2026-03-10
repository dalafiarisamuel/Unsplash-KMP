package ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.ui.model.Theme
import org.jetbrains.compose.resources.stringResource
import ui.theme.ColorDarkGray
import ui.theme.ColorLightGray
import ui.theme.ColorMatteBlack
import ui.theme.appDark
import ui.theme.appWhite
import ui.theme.colorDisabledGray
import unsplashkmp.composeapp.generated.resources.Res
import unsplashkmp.composeapp.generated.resources.appearance
import unsplashkmp.composeapp.generated.resources.dark
import unsplashkmp.composeapp.generated.resources.light
import unsplashkmp.composeapp.generated.resources.system

@Composable
internal fun ThemeSelectionSheet(currentTheme: Theme, onThemeSelected: (Theme) -> Unit) {
    Column(
        modifier =
            Modifier.fillMaxWidth()
                .background(appDark)
                .padding(top = 12.dp, start = 24.dp, end = 24.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier.width(40.dp)
                    .height(4.dp)
                    .background(
                        color = Color.Gray.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(2.dp),
                    )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.appearance),
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = appWhite,
            textAlign = TextAlign.Center,
        )

        Divider(
            modifier = Modifier.padding(vertical = 12.dp),
            color = appWhite.copy(alpha = 0.1f),
            thickness = 1.dp,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            ThemeOptionItem(
                theme = Theme.SYSTEM,
                isSelected = currentTheme == Theme.SYSTEM,
                onClick = { onThemeSelected(Theme.SYSTEM) },
            )
            ThemeOptionItem(
                theme = Theme.LIGHT,
                isSelected = currentTheme == Theme.LIGHT,
                onClick = { onThemeSelected(Theme.LIGHT) },
            )
            ThemeOptionItem(
                theme = Theme.DARK,
                isSelected = currentTheme == Theme.DARK,
                onClick = { onThemeSelected(Theme.DARK) },
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ThemeOptionItem(theme: Theme, isSelected: Boolean, onClick: () -> Unit) {
    val themeName =
        when (theme) {
            Theme.SYSTEM -> stringResource(Res.string.system)
            Theme.LIGHT -> stringResource(Res.string.light)
            Theme.DARK -> stringResource(Res.string.dark)
        }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            Modifier.clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ),
    ) {
        ThemePreview(theme = theme, isSelected = isSelected)
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier =
                Modifier.clip(CircleShape)
                    .background(if (isSelected) appWhite else colorDisabledGray.copy(alpha = 0.7f))
                    .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Text(
                text = themeName,
                color = if (isSelected) appDark else appWhite,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            )
        }
    }
}

@Composable
private fun ThemePreview(theme: Theme, isSelected: Boolean) {
    val borderColor = if (isSelected) appWhite else colorDisabledGray.copy(alpha = 0.3f)
    val borderWidth = if (isSelected) 2.dp else 1.dp

    Box(
        modifier =
            Modifier.size(width = 80.dp, height = 120.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(borderWidth, borderColor, RoundedCornerShape(24.dp))
                .background(colorDisabledGray.copy(alpha = 0.1f))
                .padding(4.dp)
    ) {
        val previewModifier = Modifier.fillMaxWidth().height(112.dp).clip(RoundedCornerShape(20.dp))

        Box(
            modifier =
                when (theme) {
                    Theme.LIGHT ->
                        previewModifier
                            .background(Color.White)
                            .border(0.5.dp, ColorLightGray, RoundedCornerShape(20.dp))
                    Theme.DARK ->
                        previewModifier
                            .background(ColorMatteBlack)
                            .border(0.5.dp, ColorDarkGray, RoundedCornerShape(20.dp))
                    Theme.SYSTEM ->
                        previewModifier
                            .background(
                                Brush.horizontalGradient(
                                    0.5f to Color.White,
                                    0.5f to ColorMatteBlack,
                                )
                            )
                            .border(0.6.dp, ColorLightGray, RoundedCornerShape(20.dp))
                }
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val circleColor =
                    when (theme) {
                        Theme.LIGHT -> ColorLightGray
                        Theme.DARK -> ColorDarkGray
                        Theme.SYSTEM -> Color.Gray.copy(alpha = 0.5f)
                    }

                Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(circleColor))

                repeat(3) {
                    Box(
                        modifier =
                            Modifier.fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(circleColor)
                    )
                }
            }
        }
    }
}
