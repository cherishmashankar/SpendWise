package com.codingchallenge.spendwise.presentation.spendwise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingchallenge.spendwise.domain.model.BalanceSummary
import com.codingchallenge.spendwise.domain.model.Transaction
import com.codingchallenge.spendwise.domain.model.TransactionType
import com.codingchallenge.spendwise.domain.usecase.preferences.PreferenceUseCases
import com.codingchallenge.spendwise.domain.usecase.transaction.TransactionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpendWiseViewModel @Inject constructor(
    private val transactionUseCases: TransactionUseCases,
    private val preferenceUseCases: PreferenceUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(SpendWiseUiState())
    val uiState: StateFlow<SpendWiseUiState> = _uiState

    init {
        observeData()
    }

    private fun observeData() {
        viewModelScope.launch {
            combine(
                transactionUseCases.getAllTransactionsUseCase(),
                preferenceUseCases.getShowChartUseCase(),
                preferenceUseCases.getLanguageUseCase(),
                preferenceUseCases.getThemeModeUseCase()
            ) { transactions, showChart, language, themeMode ->

                val totalIncome = transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
                val totalExpense = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
                val totalBalance = totalIncome - totalExpense


                SpendWiseUiState(
                    transactions = transactions,
                    filteredTransactions = applyFiltersToList(transactions),
                    balanceSummary = BalanceSummary(
                        totalBalance = totalBalance,
                        totalIncome = totalIncome,
                        totalExpense = totalExpense
                    ),
                    showChart = showChart,
                    language = language,
                    themeMode = themeMode
                )
            }.collect { newState ->
                _uiState.update { current ->
                    current.copy(
                        transactions = newState.transactions,
                        filteredTransactions = applyFiltersToList(newState.transactions),
                        balanceSummary = newState.balanceSummary,
                        showChart = newState.showChart,
                        language = newState.language,
                        themeMode = newState.themeMode,
                        isLoading = false,
                        error = null
                    )
                }
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    fun updateSelectedFilter(filter: TransactionFilter) {
        _uiState.update { it.copy(selectedFilter = filter) }
        applyFilters()
    }

    private fun applyFilters() {
        val current = _uiState.value
        val filtered = applyFiltersToList(current.transactions)
        _uiState.update { it.copy(filteredTransactions = filtered) }
    }

    private fun applyFiltersToList(transactions: List<Transaction>): List<Transaction> {
        val current = _uiState.value
        return transactions.filter { transaction ->
            val matchesSearch = transaction.title.contains(current.searchQuery, ignoreCase = true)
            val matchesType = when (current.selectedFilter) {
                TransactionFilter.All -> true
                TransactionFilter.Income -> transaction.type == TransactionType.INCOME
                TransactionFilter.Expense -> transaction.type == TransactionType.EXPENSE
            }
            matchesSearch && matchesType
        }
    }

    fun addOrUpdateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionUseCases.insertTransactionUseCase(transaction)
        }
    }

    fun deleteTransaction(id: Int) {
        viewModelScope.launch {
            val transaction = transactionUseCases.getTransactionByIdUseCase(id)
            transaction?.let {
                transactionUseCases.deleteTransactionUseCase(it)
            }
        }
    }

    fun toggleChartVisibility() {
        viewModelScope.launch {
            val current = _uiState.value.showChart
            preferenceUseCases.setShowChartUseCase(!current)
        }
    }

    fun setLanguage(lang: String) {
        viewModelScope.launch {
            preferenceUseCases.setLanguageUseCase(lang)
        }
    }

    fun setTheme(mode: String) {
        viewModelScope.launch {
            preferenceUseCases.setThemeModeUseCase(mode)
        }
    }


}
