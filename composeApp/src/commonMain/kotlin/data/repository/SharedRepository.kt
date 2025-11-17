package data.repository

internal interface SharedRepository {
    var isDarkThemeEnabled: Boolean

    fun flipTheme() {
        isDarkThemeEnabled = isDarkThemeEnabled.not()
    }
}