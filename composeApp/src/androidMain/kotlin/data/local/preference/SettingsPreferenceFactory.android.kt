package data.local.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

actual class SettingsPreferenceFactory(val context: Context) {
    actual fun createDataStore(): DataStore<Preferences> {
        val appContext = context.applicationContext
        return createDataStoreWithPath {
            appContext.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath
        }
    }
}
