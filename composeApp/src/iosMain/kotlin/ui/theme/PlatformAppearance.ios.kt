package ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import platform.UIKit.UIApplication
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.setStatusBarStyle

@Composable
actual fun PlatformAppearance(isDarkTheme: Boolean) {
    LaunchedEffect(isDarkTheme) {
        UIApplication.sharedApplication.setStatusBarStyle(
            if (isDarkTheme) UIStatusBarStyleDarkContent else UIStatusBarStyleLightContent
        )
    }
}