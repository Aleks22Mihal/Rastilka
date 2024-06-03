package com.rastilka.data.data_source.Internal_storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.rastilka.presentation.ui.theme.data.DarkMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
        val USER_SESSION_KEY = stringPreferencesKey("user_session")
        val SELECTED_LANGUAGE = stringPreferencesKey("selected_language")

        //    val FILTER_TASKS_BY_USERS = stringPreferencesKey("filter_tasks_by_user")
        val DARK_MODE = stringPreferencesKey("dark_mode")
    }

    val selectedLanguage: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[SELECTED_LANGUAGE] ?: ""
        }

    suspend fun saveSelectedLanguage(selectedLanguage: String) {
        context.dataStore.edit { preferences ->
            preferences[SELECTED_LANGUAGE] = selectedLanguage
        }
    }

    val session: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USER_SESSION_KEY] ?: ""
        }

    suspend fun saveSession(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_SESSION_KEY] = name
        }
    }
    val darkMode: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[DARK_MODE] ?: DarkMode.IsDarkModeAuto.name
        }

    suspend fun saveDarkMode(darkMode: DarkMode) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE] = darkMode.name
        }
    }
}
