package data.ui.repository

import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {

    val isDarkMode: Flow<Boolean>

    suspend fun setDarkMode(enabled: Boolean)
}
