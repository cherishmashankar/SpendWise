package com.codingchallenge.spendwise.domain.usecase.preferences

import com.codingchallenge.spendwise.domain.repository.PreferencesRepository
import javax.inject.Inject

class SetLanguageUseCase @Inject constructor(
    private val repository: PreferencesRepository
) {
    suspend operator fun invoke(lang: String) = repository.setLanguage(lang)
}
