package com.tamuno.unsplash.kmp.widget

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

val PHOTOS_KEY = stringSetPreferencesKey("photos")
val TOTAL_FAVOURITES_KEY = intPreferencesKey("total_favourites")
