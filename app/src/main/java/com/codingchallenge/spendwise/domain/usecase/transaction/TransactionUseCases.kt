package com.codingchallenge.spendwise.domain.usecase.transaction


data class TransactionUseCases(
    val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    val insertTransactionUseCase: InsertTransactionUseCase,
    val deleteTransactionUseCase: DeleteTransactionUseCase,
    val getTotalBalanceUseCase: GetTotalBalanceUseCase
)
