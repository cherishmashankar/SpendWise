package com.codingchallenge.spendwise.data.local.db

import androidx.room.TypeConverter
import com.codingchallenge.spendwise.domain.model.TransactionType

class Converters {

    @TypeConverter
    fun fromTransactionType(value: TransactionType): String = value.name

    @TypeConverter
    fun toTransactionType(value: String): TransactionType = TransactionType.valueOf(value)
}