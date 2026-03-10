package data.ui.repository

import data.ui.model.Theme
import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {

    val theme: Flow<Theme>

    suspend fun setTheme(theme: Theme)

    val savedSearchQuery: Flow<List<String>>

    suspend fun saveSearchQuery(query: String)

    suspend fun clearSavedSearchQuery()

    suspend fun deleteSavedSearchQuery(query: String)
}
