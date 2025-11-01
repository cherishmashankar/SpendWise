package com.codingchallenge.spendwise.domain.usecase.preferences

import com.codingchallenge.spendwise.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetShowChartUseCase @Inject constructor(
    private val repository: PreferencesRepository
) {
    operator fun invoke(): Flow<Boolean> = repository.getShowChart()
}
