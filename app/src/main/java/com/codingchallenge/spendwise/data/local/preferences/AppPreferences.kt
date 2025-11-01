package com.codingchallenge.spendwise.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "app_preferences"
val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

class AppPreferences(private val context: Context) {

    val themeMode: Flow<String> = context.dataStore.data
        .map { it[PreferencesKeys.THEME_MODE] ?: "system" }

    val language: Flow<String> = context.dataStore.data
        .map { it[PreferencesKeys.LANGUAGE] ?: "en" }

    val showChart: Flow<Boolean> = context.dataStore.data
        .map { it[PreferencesKeys.SHOW_CHART] ?: true }

    suspend fun setThemeMode(mode: String) {
        context.dataStore.edit { it[PreferencesKeys.THEME_MODE] = mode }
    }

    suspend fun setLanguage(lang: String) {
        context.dataStore.edit { it[PreferencesKeys.LANGUAGE] = lang }
    }

    suspend fun setShowChart(value: Boolean) {
        context.dataStore.edit { it[PreferencesKeys.SHOW_CHART] = value }
    }
}