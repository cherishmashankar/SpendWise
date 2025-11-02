package com.codingchallenge.spendwise.domain.usecase.preferences

data class PreferenceUseCases(
    val getThemeModeUseCase: GetThemeModeUseCase,
    val setThemeModeUseCase: SetThemeModeUseCase,
    val getLanguageUseCase: GetLanguageUseCase,
    val setLanguageUseCase: SetLanguageUseCase,
    val getShowChartUseCase: GetShowChartUseCase,
    val setShowChartUseCase: SetShowChartUseCase
)