package com.example.rda1_lab7_resilientform_sebastiansalcedo.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(
    name = "user_prefs"
)

class UserPreferences(
    private val context: Context
) {

    private val NAME_KEY =
        stringPreferencesKey("user_name")

    // Flujo para leer el nombre guardado en disco
    val userName: Flow<String> =
        context.dataStore.data.map { preferences ->
            preferences[NAME_KEY] ?: ""
        }

    // Guarda el nombre en DataStore
    suspend fun saveName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[NAME_KEY] = name
        }
    }
}