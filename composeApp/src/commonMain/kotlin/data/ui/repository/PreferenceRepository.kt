package data.ui.repository

import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {

    val isDarkMode: Flow<Boolean>

    suspend fun setDarkMode(enabled: Boolean)

    val savedSearchQuery: Flow<List<String>>

    suspend fun saveSearchQuery(query: String)

    suspend fun clearSavedSearchQuery()

    suspend fun deleteSavedSearchQuery(query: String)
}
