package com.codingchallenge.spendwise.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codingchallenge.spendwise.domain.model.TransactionType

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val amount: Double,
    val type: TransactionType,
    val category: String? = null,
    val notes: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val lastModified: Long = System.currentTimeMillis(),
)