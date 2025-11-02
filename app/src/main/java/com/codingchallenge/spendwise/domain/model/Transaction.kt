package com.codingchallenge.spendwise.domain.model

data class Transaction(
    val id: Int = 0,
    val title: String,
    val amount: Double,
    val type: TransactionType,
    val category: String? = null,
    val notes: String? = null,
    val timestamp: Long,
    val lastModified: Long,
)