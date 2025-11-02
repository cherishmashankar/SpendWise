package com.codingchallenge.spendwise.presentation.spendwise

import com.codingchallenge.spendwise.domain.model.BalanceSummary
import com.codingchallenge.spendwise.domain.model.Transaction
import com.codingchallenge.spendwise.utils.AppConstants

data class SpendWiseUiState(
    val transactions: List<Transaction> = emptyList(),
    val filteredTransactions: List<Transaction> = emptyList(),
    val balanceSummary: BalanceSummary = BalanceSummary(
        totalBalance = 0.0,
        totalIncome = 0.0,
        totalExpense = 0.0
    ),
    val savingPercent: Int = 0,
    val showChart: Boolean = true,
    val language: String =  AppConstants.LANGUAGE_ENGLISH,
    val themeMode: String = AppConstants.THEME_DARK,
    val searchQuery: String = "",
    val selectedFilter: TransactionFilter = TransactionFilter.All,
    val isLoading: Boolean = false,
    val error: String? = null
)

enum class TransactionFilter {
    All, Income, Expense
}
