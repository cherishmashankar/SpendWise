package com.codingchallenge.spendwise.domain.usecase.preferences

import com.codingchallenge.spendwise.domain.repository.PreferencesRepository
import javax.inject.Inject

class SetThemeModeUseCase @Inject constructor(
    private val repository: PreferencesRepository
) {
    suspend operator fun invoke(mode: String) = repository.setThemeMode(mode)
}
