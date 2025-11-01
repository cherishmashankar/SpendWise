package com.codingchallenge.spendwise.domain.usecase.preferences

import com.codingchallenge.spendwise.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThemeModeUseCase @Inject constructor(
    private val repository: PreferencesRepository
) {
    operator fun invoke(): Flow<String> = repository.getThemeMode()
}
