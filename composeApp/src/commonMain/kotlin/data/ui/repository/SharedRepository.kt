package data.ui.repository

internal interface SharedRepository {
    var isDarkThemeEnabled: Boolean

    fun flipTheme() {
        isDarkThemeEnabled = isDarkThemeEnabled.not()
    }
}