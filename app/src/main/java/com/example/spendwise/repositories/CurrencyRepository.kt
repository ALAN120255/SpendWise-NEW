package com.example.spendwise.repositories

import android.content.Context
import android.content.SharedPreferences

class CurrencyRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("spendwise_prefs", Context.MODE_PRIVATE)

    fun getCurrencySymbol(): String = when (prefs.getString("currency", "USD")) {
        "USD" -> "$"
        "EUR" -> "€"
        "GBP" -> "£"
        "ZAR" -> "R"
        "JPY" -> "¥"
        "CAD" -> "CA$"
        "AUD" -> "A$"
        else -> "$"
    }

    fun getCurrency(): String = prefs.getString("currency", "USD") ?: "USD"

    fun setCurrency(currency: String) = prefs.edit().putString("currency", currency).apply()

    fun isDarkMode(): Boolean = prefs.getBoolean("dark_mode", false)

    fun setDarkMode(enabled: Boolean) = prefs.edit().putBoolean("dark_mode", enabled).apply()
}
