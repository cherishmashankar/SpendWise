package com.codingchallenge.spendwise.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun getThemeMode(): Flow<String>
    suspend fun setThemeMode(mode: String)
    fun getLanguage(): Flow<String>
    suspend fun setLanguage(lang: String)
    fun getShowChart(): Flow<Boolean>
    suspend fun setShowChart(value: Boolean)
}