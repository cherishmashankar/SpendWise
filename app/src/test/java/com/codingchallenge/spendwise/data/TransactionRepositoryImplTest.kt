package com.codingchallenge.spendwise.data

import app.cash.turbine.test
import com.codingchallenge.spendwise.data.local.db.TransactionDao
import com.codingchallenge.spendwise.data.mapper.toDomain
import com.codingchallenge.spendwise.data.repository.TransactionRepositoryImpl
import com.codingchallenge.spendwise.domain.model.Transaction
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat

class TransactionRepositoryImplTest {

    private lateinit var repository: TransactionRepositoryImpl
    private lateinit var dao: TransactionDao

    private val fakeEntity = mockk<com.codingchallenge.spendwise.data.local.db.TransactionEntity>(relaxed = true)
    private val fakeDomain = mockk<Transaction>(relaxed = true)

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        repository = TransactionRepositoryImpl(dao)
    }

    @Test
    fun `getAllTransactions emits mapped domain list`() = runTest {
        val flow = MutableStateFlow(listOf(fakeEntity))
        every { dao.getAllTransactions() } returns flow

        repository.getAllTransactions().test {
            val result = awaitItem()
            assertThat(result).isNotEmpty()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getAllTransactions emits empty list when dao is empty`() = runTest {
        val flow = MutableStateFlow<List<com.codingchallenge.spendwise.data.local.db.TransactionEntity>>(emptyList())
        every { dao.getAllTransactions() } returns flow

        repository.getAllTransactions().test {
            val result = awaitItem()
            assertThat(result).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getTransactionById returns mapped transaction`() = runTest {
        coEvery { dao.getTransactionById(1) } returns fakeEntity

        val result = repository.getTransactionById(1)
        assertThat(result).isEqualTo(fakeEntity.toDomain())
    }

    @Test
    fun `getTransactionById returns null when dao returns null`() = runTest {
        coEvery { dao.getTransactionById(any()) } returns null
        val result = repository.getTransactionById(999)
        assertThat(result).isNull()
    }

    @Test
    fun `insertTransaction calls dao`() = runTest {
        val fakeTransaction = mockk<Transaction>(relaxed = true)
        repository.insertTransaction(fakeTransaction)
        coVerify { dao.insertTransaction(any()) }
    }

    @Test
    fun `deleteTransaction calls dao`() = runTest {
        val fakeTransaction = mockk<Transaction>(relaxed = true)
        repository.deleteTransaction(fakeTransaction)
        coVerify { dao.deleteTransaction(any()) }
    }

    @Test
    fun `insertTransaction can be called multiple times without error`() = runTest {
        val fakeTransaction = mockk<Transaction>(relaxed = true)
        repeat(3) {
            repository.insertTransaction(fakeTransaction)
        }
        coVerify(exactly = 3) { dao.insertTransaction(any()) }
    }

    @Test
    fun `getAllTransactions handles dao emitting null`() = runTest {
        val flow = MutableStateFlow<List<com.codingchallenge.spendwise.data.local.db.TransactionEntity>?>(null)
        @Suppress("UNCHECKED_CAST")
        every { dao.getAllTransactions() } returns flow as MutableStateFlow<List<com.codingchallenge.spendwise.data.local.db.TransactionEntity>>

        repository.getAllTransactions().test {
            cancelAndIgnoreRemainingEvents()
        }
    }
}
