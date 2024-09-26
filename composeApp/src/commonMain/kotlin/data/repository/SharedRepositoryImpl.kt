package data.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

internal class SharedRepositoryImpl : SharedRepository {
    override var isDarkThemeEnabled: Boolean by mutableStateOf(false)
}
