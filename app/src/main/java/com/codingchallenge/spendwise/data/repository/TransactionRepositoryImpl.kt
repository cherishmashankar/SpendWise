package com.codingchallenge.spendwise.data.repository

import com.codingchallenge.spendwise.data.local.db.TransactionDao
import com.codingchallenge.spendwise.data.mapper.toDomain
import com.codingchallenge.spendwise.data.mapper.toEntity
import com.codingchallenge.spendwise.domain.model.Transaction
import com.codingchallenge.spendwise.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val dao: TransactionDao
) : TransactionRepository {

    override fun getAllTransactions(): Flow<List<Transaction>> {
        return dao.getAllTransactions().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getTransactionById(id: Int): Transaction? {
        return dao.getTransactionById(id)?.toDomain()
    }

    override suspend fun insertTransaction(transaction: Transaction) {
        dao.insertTransaction(transaction.toEntity())
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        dao.deleteTransaction(transaction.toEntity())
    }
}
