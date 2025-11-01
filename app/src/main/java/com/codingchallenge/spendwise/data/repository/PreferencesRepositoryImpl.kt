package com.codingchallenge.spendwise.data.repository

import com.codingchallenge.spendwise.data.local.preferences.AppPreferences
import com.codingchallenge.spendwise.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepositoryImpl @Inject constructor(
    private val appPreferences: AppPreferences
) : PreferencesRepository {

    override fun getThemeMode(): Flow<String> = appPreferences.themeMode
    override suspend fun setThemeMode(mode: String) = appPreferences.setThemeMode(mode)
    override fun getLanguage(): Flow<String> = appPreferences.language
    override suspend fun setLanguage(lang: String) = appPreferences.setLanguage(lang)
    override fun getShowChart(): Flow<Boolean> = appPreferences.showChart
    override suspend fun setShowChart(value: Boolean) = appPreferences.setShowChart(value)
}