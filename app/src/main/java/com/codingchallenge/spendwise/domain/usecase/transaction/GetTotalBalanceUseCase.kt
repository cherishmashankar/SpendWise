package com.codingchallenge.spendwise.domain.usecase.transaction

import com.codingchallenge.spendwise.domain.model.BalanceSummary
import com.codingchallenge.spendwise.domain.model.TransactionType
import com.codingchallenge.spendwise.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject



class GetTotalBalanceUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(): Flow<BalanceSummary> {
        return repository.getAllTransactions().map { transactions ->
            val income = transactions
                .filter { it.type == TransactionType.INCOME }
                .sumOf { it.amount }

            val expense = transactions
                .filter { it.type == TransactionType.EXPENSE }
                .sumOf { it.amount }

            BalanceSummary(
                totalBalance = income - expense,
                totalIncome = income,
                totalExpense = expense
            )
        }
    }
}
