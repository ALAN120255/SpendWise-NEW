package com.example.spendwise

import java.text.SimpleDateFormat
import java.util.*

data class UserProfile(
    val uid: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val gender: String = "",
    val email: String = "",
    val profileImageUrl: String = ""
) {
    fun fullName() = "$firstName $lastName".trim()
    fun initials(): String {
        val f = firstName.firstOrNull()?.uppercaseChar() ?: ' '
        val l = lastName.firstOrNull()?.uppercaseChar() ?: ' '
        return "$f$l".trim().ifBlank { email.firstOrNull()?.uppercaseChar()?.toString() ?: "?" }
    }
}

data class Expense(
    val id: String = "",
    val amount: Double = 0.0,
    val category: String = "",
    val description: String = "",
    val date: Long = System.currentTimeMillis(),
    val isRecurring: Boolean = false,
    val photoUrl: String = ""
) {
    fun getFormattedDate(): String =
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(date))

    fun getCategoryIcon(): String = when (category.lowercase()) {
        "food"          -> "🍔"
        "transport"     -> "🚗"
        "shopping"      -> "🛍️"
        "health"        -> "💊"
        "entertainment" -> "🎬"
        "utilities"     -> "💡"
        "rent"          -> "🏠"
        "education"     -> "📚"
        else            -> "💳"
    }

    fun isEssential(): Boolean = category.lowercase() in listOf(
        "food", "transport", "health", "utilities", "rent", "education"
    )
}

data class Income(
    val id: String = "",
    val amount: Double = 0.0,
    val category: String = "",
    val description: String = "",
    val date: Long = System.currentTimeMillis(),
    val isRecurring: Boolean = false
) {
    fun getFormattedDate(): String =
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(date))

    fun getCategoryIcon(): String = when (category.lowercase()) {
        "salary"     -> "💼"
        "freelance"  -> "💻"
        "investment" -> "📈"
        "gift"       -> "🎁"
        "rental"     -> "🏠"
        else         -> "💰"
    }
}

data class TransactionItem(
    val id: String,
    val amount: Double,
    val category: String,
    val description: String,
    val date: Long,
    val isExpense: Boolean,
    val icon: String,
    val photoUrl: String = ""
)

// ── Gamification (income-based, essential-spend rewards) ───────────────────
data class UserLevel(
    val level: Int,
    val title: String,
    val xp: Int,
    val xpForNext: Int,
    val badge: String,
    val unlockCondition: String
)

/**
 * Essential categories are: Food, Transport, Health, Utilities, Rent, Education.
 * XP is earned by:
 *   - Spending on essential needs (10 XP per essential transaction, capped at 100)
 *   - Keeping essential spend ratio healthy (>= 50% of expenses go to essentials → +75 XP)
 *   - Spending within income (totalSpent <= totalIncome → +75 XP)
 *   - Saving at least 10% of income (→ +50 XP)
 *   - Saving at least 20% of income (→ +100 XP)
 */
object XPSystem {
    val levels = listOf(
        UserLevel(1, "Needs Novice",       0,   100, "🌱", "Log first 5 essential purchases"),
        UserLevel(2, "Smart Spender",      100, 250, "💡", "Keep 50%+ of spending on essentials"),
        UserLevel(3, "Savings Sidekick",   250, 500, "🎯", "Save at least 10% of your income"),
        UserLevel(4, "Essentials Expert",  500, 800, "🏆", "Save 20%+ and spend within income"),
        UserLevel(5, "Budget Ninja",       800, 800, "🥷", "Master all goals consistently")
    )

    fun calculateXP(
        expenses: List<Expense>,
        totalIncome: Double
    ): Int {
        var xp = 0
        val essentialExpenses = expenses.filter { it.isEssential() }
        val totalSpent = expenses.sumOf { it.amount }
        val essentialSpent = essentialExpenses.sumOf { it.amount }

        // Up to 100 XP for essential transactions logged
        xp += minOf(essentialExpenses.size * 10, 100)

        // 75 XP if >= 50% of spending is on essential needs
        if (totalSpent > 0 && essentialSpent / totalSpent >= 0.5) xp += 75

        // 75 XP for spending within income
        if (totalIncome > 0 && totalSpent <= totalIncome) xp += 75

        // 50 XP for saving 10%+ of income
        val savingsRatio = if (totalIncome > 0) (totalIncome - totalSpent) / totalIncome else 0.0
        if (savingsRatio >= 0.10) xp += 50

        // 100 XP for saving 20%+ of income
        if (savingsRatio >= 0.20) xp += 100

        return xp.coerceAtLeast(0)
    }

    fun getLevelForXP(xp: Int): UserLevel = levels.lastOrNull { xp >= it.xp } ?: levels.first()

    fun getProgressPercent(xp: Int): Int {
        val current = getLevelForXP(xp)
        if (current.level == 5) return 100
        val next = levels.getOrNull(current.level) ?: return 100
        val range = next.xp - current.xp
        val earned = xp - current.xp
        return ((earned.toFloat() / range) * 100).toInt().coerceIn(0, 100)
    }
}

data class CustomCategory(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val icon: String,
    val color: String,
    val budgetLimit: Double = 0.0
)

data class CategorySummary(
    val name: String,
    val icon: String,
    val totalSpent: Double,
    val transactionCount: Int,
    val color: String,
    val budgetLimit: Double
) {
    fun isOverBudget(): Boolean = budgetLimit > 0 && totalSpent > budgetLimit
}
