package com.codingchallenge.spendwise.utils


import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


fun Long.toFormattedDate(locale: Locale): String {
    return SimpleDateFormat("MMM dd, yyyy", locale).format(Date(this))
}

fun Double.toEuroFormat(locale: Locale): String {
    val formatter = NumberFormat.getNumberInstance(locale)
    formatter.minimumFractionDigits = 2
    formatter.maximumFractionDigits = 2
    val formatted = formatter.format(this)


    return "€$formatted"
}
