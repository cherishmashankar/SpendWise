package com.codingchallenge.spendwise.data.mapper

import com.codingchallenge.spendwise.data.local.db.TransactionEntity
import com.codingchallenge.spendwise.domain.model.Transaction

fun TransactionEntity.toDomain(): Transaction {
    return Transaction(
        id = id,
        title = title,
        amount = amount,
        type = type,
        category = category,
        notes = notes,
        timestamp = timestamp,
        lastModified = lastModified,
        currency = currency
    )
}

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id,
        title = title,
        amount = amount,
        type = type,
        category = category,
        notes = notes,
        timestamp = timestamp,
        lastModified = lastModified,
        currency = currency
    )
}
