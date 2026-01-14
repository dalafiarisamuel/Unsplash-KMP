package data.local.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

expect class SettingsPreferenceFactory {
    fun createDataStore(): DataStore<Preferences>
}
