package data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import data.ui.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

internal class PreferenceRepositoryImpl(private val dataStore: DataStore<Preferences>) :
    PreferenceRepository {
    private val themeKey = booleanPreferencesKey("dark_theme")

    override val isDarkMode: Flow<Boolean> =
        dataStore.data
            .catch { exception ->
                exception.printStackTrace()
                emit(emptyPreferences())
            }
            .map { preferences -> preferences[themeKey] ?: false }

    override suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { preferences -> preferences[themeKey] = enabled }
    }
}
