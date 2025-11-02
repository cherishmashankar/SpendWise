package com.codingchallenge.spendwise.presentation.spendwise

import app.cash.turbine.test
import com.codingchallenge.spendwise.domain.model.BalanceSummary
import com.codingchallenge.spendwise.domain.model.Transaction
import com.codingchallenge.spendwise.domain.model.TransactionType
import com.codingchallenge.spendwise.domain.usecase.preferences.*
import com.codingchallenge.spendwise.domain.usecase.transaction.TransactionUseCases
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SpendWiseViewModelTest {

    private lateinit var viewModel: SpendWiseViewModel
    private lateinit var transactionUseCases: TransactionUseCases
    private lateinit var preferenceUseCases: PreferenceUseCases

    private val dispatcher = StandardTestDispatcher()

    private val fakeTransactions = listOf(
        Transaction(1, "Salary", 1000.0, TransactionType.INCOME, "Job", "", 0L, 0L),
        Transaction(2, "Groceries", 200.0, TransactionType.EXPENSE, "Food", "", 0L, 0L)
    )

    // preference mocks
    private lateinit var getShowChartUseCase: GetShowChartUseCase
    private lateinit var getLanguageUseCase: GetLanguageUseCase
    private lateinit var getThemeModeUseCase: GetThemeModeUseCase
    private lateinit var setShowChartUseCase: SetShowChartUseCase
    private lateinit var setLanguageUseCase: SetLanguageUseCase
    private lateinit var setThemeModeUseCase: SetThemeModeUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        // ✅ Mock all Preference use cases
        getShowChartUseCase = mockk(relaxed = true)
        getLanguageUseCase = mockk(relaxed = true)
        getThemeModeUseCase = mockk(relaxed = true)
        setShowChartUseCase = mockk(relaxed = true)
        setLanguageUseCase = mockk(relaxed = true)
        setThemeModeUseCase = mockk(relaxed = true)

        every { getShowChartUseCase.invoke() } returns flowOf(true)
        every { getLanguageUseCase.invoke() } returns flowOf("en")
        every { getThemeModeUseCase.invoke() } returns flowOf("dark")

        // ✅ Mock Transaction use cases
        transactionUseCases = mockk(relaxed = true)
        every { transactionUseCases.getAllTransactionsUseCase() } returns flowOf(fakeTransactions)

        // ✅ Build the composite PreferenceUseCases
        preferenceUseCases = PreferenceUseCases(
            getShowChartUseCase = getShowChartUseCase,
            getLanguageUseCase = getLanguageUseCase,
            getThemeModeUseCase = getThemeModeUseCase,
            setShowChartUseCase = setShowChartUseCase,
            setLanguageUseCase = setLanguageUseCase,
            setThemeModeUseCase = setThemeModeUseCase
        )

        viewModel = SpendWiseViewModel(transactionUseCases, preferenceUseCases)
    }

    // ---------- UI STATE TESTS ----------

    @Test
    fun `initial uiState has correct balance summary`() = runTest {
        advanceUntilIdle()
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.balanceSummary.totalIncome).isEqualTo(1000.0)
            assertThat(state.balanceSummary.totalExpense).isEqualTo(200.0)
            assertThat(state.balanceSummary.totalBalance).isEqualTo(800.0)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `updateSearchQuery filters transactions by title`() = runTest {
        viewModel.updateSearchQuery("gro")
        advanceUntilIdle()
        val filtered = viewModel.uiState.value.filteredTransactions
        advanceUntilIdle()
        assertThat(filtered.size).isEqualTo(1)
        assertThat(filtered.first().title).isEqualTo("Groceries")
    }

    @Test
    fun `updateSelectedFilter filters only income`() = runTest {
        viewModel.updateSelectedFilter(TransactionFilter.Income)
        val filtered = viewModel.uiState.value.filteredTransactions
        assertThat(filtered.all { it.type == TransactionType.INCOME }).isTrue()
    }

    @Test
    fun `updateSelectedFilter filters only expense`() = runTest {
        viewModel.updateSelectedFilter(TransactionFilter.Expense)
        val filtered = viewModel.uiState.value.filteredTransactions
        assertThat(filtered.all { it.type == TransactionType.EXPENSE }).isTrue()
    }

    @Test
    fun `empty transaction list results in zero balance summary`() = runTest {
        every { transactionUseCases.getAllTransactionsUseCase() } returns flowOf(emptyList())
        viewModel = SpendWiseViewModel(transactionUseCases, preferenceUseCases)
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.balanceSummary).isEqualTo(BalanceSummary(0.0, 0.0, 0.0))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `search query that matches nothing returns empty filtered list`() = runTest {
        viewModel.updateSearchQuery("xyz")
        advanceUntilIdle()
        val filtered = viewModel.uiState.value.filteredTransactions
        assertThat(filtered).isEmpty()
    }


    @Test
    fun `addOrUpdateTransaction calls insert use case`() = runTest {
        val newTransaction = Transaction(3, "Bonus", 300.0, TransactionType.INCOME, "Job", "", 0L, 0L)
        viewModel.addOrUpdateTransaction(newTransaction)
        advanceUntilIdle()
        coVerify { transactionUseCases.insertTransactionUseCase(newTransaction) }
    }

    @Test
    fun `deleteTransaction calls delete use case when transaction exists`() = runTest {
        val transaction = fakeTransactions.first()
        coEvery { transactionUseCases.getTransactionByIdUseCase(1) } returns transaction
        viewModel.deleteTransaction(1)
        advanceUntilIdle()
        coVerify { transactionUseCases.deleteTransactionUseCase(transaction) }
    }

    @Test
    fun `deleteTransaction does nothing when transaction not found`() = runTest {
        coEvery { transactionUseCases.getTransactionByIdUseCase(any()) } returns null
        viewModel.deleteTransaction(99)
        coVerify(exactly = 0) { transactionUseCases.deleteTransactionUseCase(any()) }
    }

    @Test
    fun `toggleChartVisibility calls setShowChart with opposite value`() = runTest {
        viewModel.toggleChartVisibility()
        advanceUntilIdle()
        coVerify(exactly = 1) { setShowChartUseCase.invoke(false) }
    }

    @Test
    fun `setLanguage calls setLanguageUseCase`() = runTest {
        viewModel.setLanguage("de")
        advanceUntilIdle()
        coVerify(exactly = 1) { setLanguageUseCase.invoke("de") }
    }

    @Test
    fun `setTheme calls setThemeModeUseCase`() = runTest {
        viewModel.setTheme("light")
        advanceUntilIdle()
        coVerify(exactly = 1) { setThemeModeUseCase.invoke("light") }
    }

    @Test
    fun `search and filter combined only returns matching income transactions`() = runTest {
        advanceUntilIdle() // wait for initial data

        viewModel.updateSelectedFilter(TransactionFilter.Income)
        viewModel.updateSearchQuery("sal")

        advanceUntilIdle()
        val filtered = viewModel.uiState.value.filteredTransactions

        assertThat(filtered.size).isEqualTo(1)
        assertThat(filtered.first().title).isEqualTo("Salary")
        assertThat(filtered.first().type).isEqualTo(TransactionType.INCOME)
    }


    @Test
    fun `empty search and All filter returns all transactions`() = runTest {
        advanceUntilIdle()
        viewModel.updateSearchQuery("")
        viewModel.updateSelectedFilter(TransactionFilter.All)

        advanceUntilIdle()
        val filtered = viewModel.uiState.value.filteredTransactions

        assertThat(filtered).hasSize(fakeTransactions.size)
        assertThat(filtered).containsExactlyElementsIn(fakeTransactions)
    }

    @Test
    fun `clearing filter resets filteredTransactions to full list`() = runTest {
        advanceUntilIdle()
        viewModel.updateSelectedFilter(TransactionFilter.Expense)
        viewModel.updateSelectedFilter(TransactionFilter.All)
        advanceUntilIdle()
        val filtered = viewModel.uiState.value.filteredTransactions
        assertThat(filtered).containsExactlyElementsIn(fakeTransactions)
    }

}
