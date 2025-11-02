package com.codingchallenge.spendwise.utils


import android.content.Context
import android.content.res.Configuration
import java.util.*

fun Context.updateLocale(languageCode: String): Context {
    val locale = when (languageCode.lowercase()) {
        "de" -> Locale.GERMAN
        else -> Locale.ENGLISH
    }
    Locale.setDefault(locale)
    val config = Configuration(resources.configuration)
    config.setLocale(locale)
    return createConfigurationContext(config)
}
