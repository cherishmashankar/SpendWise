package com.codingchallenge.spendwise.domain.usecase.transaction

import com.codingchallenge.spendwise.domain.model.Transaction
import com.codingchallenge.spendwise.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(): Flow<List<Transaction>> = repository.getAllTransactions()
}
