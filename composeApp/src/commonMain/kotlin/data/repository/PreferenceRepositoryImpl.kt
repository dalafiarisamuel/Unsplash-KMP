package data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import data.ui.model.Theme
import data.ui.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

internal class PreferenceRepositoryImpl(private val dataStore: DataStore<Preferences>) :
    PreferenceRepository {
    private val themeKey = stringPreferencesKey("theme")
    private val savedSearchKey = stringSetPreferencesKey("saved_search_query")

    override val theme: Flow<Theme> =
        dataStore.data
            .catch { emit(emptyPreferences()) }
            .map { preferences -> Theme.valueOfOrDefault(preferences[themeKey]) }

    override suspend fun setTheme(theme: Theme) {
        dataStore.edit { preferences -> preferences[themeKey] = theme.name }
    }

    override val savedSearchQuery: Flow<List<String>> =
        dataStore.data
            .catch { emit(emptyPreferences()) }
            .map { preferences -> preferences[savedSearchKey]?.toList() ?: emptyList() }

    override suspend fun saveSearchQuery(query: String) {
        dataStore.edit { preferences ->
            val savedSearch = (preferences[savedSearchKey] ?: emptySet()).toMutableSet()
            savedSearch.add(query)
            preferences[savedSearchKey] = savedSearch
        }
    }

    override suspend fun clearSavedSearchQuery() {
        dataStore.edit { preferences -> preferences[savedSearchKey] = emptySet() }
    }

    override suspend fun deleteSavedSearchQuery(query: String) {
        dataStore.edit { preferences ->
            val savedSearch = (preferences[savedSearchKey] ?: emptySet()).toMutableSet()
            savedSearch.remove(query)
            preferences[savedSearchKey] = savedSearch
        }
    }
}
