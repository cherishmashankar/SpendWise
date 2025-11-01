package com.codingchallenge.spendwise.domain.usecase.transaction

import com.codingchallenge.spendwise.domain.model.Transaction
import com.codingchallenge.spendwise.domain.repository.TransactionRepository
import javax.inject.Inject

class InsertTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction) = repository.insertTransaction(transaction)
}
