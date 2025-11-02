package com.codingchallenge.spendwise.data

import app.cash.turbine.test
import com.codingchallenge.spendwise.data.local.preferences.AppPreferences
import com.codingchallenge.spendwise.data.repository.PreferencesRepositoryImpl
import com.codingchallenge.spendwise.utils.AppConstants
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat

class PreferencesRepositoryImplTest {

    private lateinit var repository: PreferencesRepositoryImpl
    private lateinit var appPreferences: AppPreferences

    private val themeModeFlow = MutableStateFlow(AppConstants.THEME_LIGHT)
    private val languageFlow = MutableStateFlow(AppConstants.LANGUAGE_ENGLISH)
    private val showChartFlow = MutableStateFlow(true)

    @Before
    fun setup() {
        appPreferences = mockk(relaxed = true)

        // Mock Flow returns
        coEvery { appPreferences.themeMode } returns themeModeFlow
        coEvery { appPreferences.language } returns languageFlow
        coEvery { appPreferences.showChart } returns showChartFlow

        repository = PreferencesRepositoryImpl(appPreferences)
    }

    @Test
    fun `getThemeMode returns current value`() = runTest {
        repository.getThemeMode().test {
            assertThat(awaitItem()).isEqualTo(AppConstants.THEME_LIGHT)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `setThemeMode updates preference`() = runTest {
        repository.setThemeMode(AppConstants.THEME_DARK)
        coVerify { appPreferences.setThemeMode(AppConstants.THEME_DARK) }
    }

    @Test
    fun `getLanguage emits expected value`() = runTest {
        repository.getLanguage().test {
            assertThat(awaitItem()).isEqualTo(AppConstants.LANGUAGE_ENGLISH)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `setLanguage delegates to appPreferences`() = runTest {
        repository.setLanguage(AppConstants.LANGUAGE_GERMAN)
        coVerify { appPreferences.setLanguage(AppConstants.LANGUAGE_GERMAN) }
    }

    @Test
    fun `getShowChart emits expected value`() = runTest {
        repository.getShowChart().test {
            assertThat(awaitItem()).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `setShowChart updates preference`() = runTest {
        repository.setShowChart(false)
        coVerify { appPreferences.setShowChart(false) }
    }

    @Test
    fun `getThemeMode reflects updates`() = runTest {
        themeModeFlow.value = AppConstants.THEME_DARK
        repository.getThemeMode().test {
            assertThat(awaitItem()).isEqualTo(AppConstants.THEME_DARK)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
