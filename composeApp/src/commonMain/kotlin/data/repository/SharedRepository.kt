package data.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class SharedRepository {
    var isDarkThemeEnabled by mutableStateOf(false)
}
