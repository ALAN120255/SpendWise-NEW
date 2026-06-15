package com.example.spendwise.repositories

import android.content.Context
import android.content.SharedPreferences
import com.example.spendwise.CustomCategory
import com.example.spendwise.Expense
import com.example.spendwise.Income
import com.example.spendwise.UserProfile
import com.example.spendwise.XPSystem
import com.example.spendwise.db.CustomCategoryEntity
import com.example.spendwise.db.ExpenseEntity
import com.example.spendwise.db.IncomeEntity
import com.example.spendwise.db.SpendWiseDatabase
import com.example.spendwise.db.UserEntity
import com.example.spendwise.utils.NotificationHelper
import java.security.MessageDigest

class AppRepository(private val context: Context) {

    private val db = SpendWiseDatabase.getInstance(context)
    private val userDao = db.userDao()
    private val expenseDao = db.expenseDao()
    private val incomeDao = db.incomeDao()
    private val customCategoryDao = db.customCategoryDao()
    private val prefs: SharedPreferences =
        context.getSharedPreferences("spendwise_prefs", Context.MODE_PRIVATE)

    // ── Session ───────────────────────────────────────────────────

    fun getCurrentUserId(): Int = prefs.getInt("logged_in_user_id", -1)

    fun isLoggedIn(): Boolean = getCurrentUserId() != -1

    fun saveSession(userId: Int) = prefs.edit().putInt("logged_in_user_id", userId).apply()

    fun clearSession() = prefs.edit().remove("logged_in_user_id").apply()

    // ── Auth ──────────────────────────────────────────────────────

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    suspend fun register(
        firstName: String, lastName: String, gender: String,
        email: String, password: String, profileImageUrl: String = ""
    ): Result<Int> {
        return try {
            val existing = userDao.findByEmail(email)
            if (existing != null) return Result.failure(Exception("Email already registered"))
            val user = UserEntity(
                firstName = firstName,
                lastName = lastName,
                gender = gender,
                email = email,
                passwordHash = hashPassword(password),
                profileImageUrl = profileImageUrl
            )
            val id = userDao.insert(user).toInt()
            saveSession(id)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<Int> {
        return try {
            val user = userDao.findByEmail(email)
                ?: return Result.failure(Exception("No account found with this email"))
            if (user.passwordHash != hashPassword(password))
                return Result.failure(Exception("Incorrect password"))
            saveSession(user.id)
            Result.success(user.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() = clearSession()

    // ── Profile ───────────────────────────────────────────────────

    suspend fun getUserProfile(): UserProfile {
        val user = userDao.findById(getCurrentUserId()) ?: return UserProfile()
        return UserProfile(
            uid = user.id.toString(),
            firstName = user.firstName,
            lastName = user.lastName,
            gender = user.gender,
            email = user.email,
            profileImageUrl = user.profileImageUrl
        )
    }

    suspend fun updateProfile(profile: UserProfile, newPassword: String? = null) {
        val existing = userDao.findById(getCurrentUserId()) ?: return
        val updated = existing.copy(
            firstName = profile.firstName,
            lastName = profile.lastName,
            gender = profile.gender,
            email = profile.email,
            profileImageUrl = profile.profileImageUrl,
            passwordHash = if (!newPassword.isNullOrBlank()) hashPassword(newPassword) else existing.passwordHash
        )
        userDao.update(updated)
    }

    // ── Budget ────────────────────────────────────────────────────

    suspend fun getBudget(): Double =
        userDao.findById(getCurrentUserId())?.monthlyBudget ?: 0.0

    suspend fun setBudget(amount: Double) =
        userDao.updateBudget(getCurrentUserId(), amount)

    // ── Expenses ──────────────────────────────────────────────────

    suspend fun addExpense(expense: Expense) {
        expenseDao.insert(
            ExpenseEntity(
                userId = getCurrentUserId(),
                amount = expense.amount,
                category = expense.category,
                description = expense.description,
                date = expense.date,
                isRecurring = expense.isRecurring,
                photoUrl = expense.photoUrl
            )
        )
        checkLevelUp()
    }

    suspend fun getExpenses(): List<Expense> =
        expenseDao.getAll(getCurrentUserId()).map {
            Expense(it.id.toString(), it.amount, it.category, it.description, it.date, it.isRecurring, it.photoUrl)
        }

    suspend fun getExpensesBetween(start: Long, end: Long): List<Expense> =
        expenseDao.getBetweenDates(getCurrentUserId(), start, end).map {
            Expense(it.id.toString(), it.amount, it.category, it.description, it.date, it.isRecurring, it.photoUrl)
        }

    suspend fun deleteExpense(expenseId: String) {
        val id = expenseId.toIntOrNull() ?: return
        expenseDao.deleteById(id, getCurrentUserId())
        checkLevelUp()
    }

    // ── Incomes ───────────────────────────────────────────────────

    suspend fun addIncome(income: Income) {
        incomeDao.insert(
            IncomeEntity(
                userId = getCurrentUserId(),
                amount = income.amount,
                category = income.category,
                description = income.description,
                date = income.date,
                isRecurring = income.isRecurring
            )
        )
        checkLevelUp()
    }

    suspend fun getIncomes(): List<Income> =
        incomeDao.getAll(getCurrentUserId()).map {
            Income(it.id.toString(), it.amount, it.category, it.description, it.date, it.isRecurring)
        }

    suspend fun deleteIncome(incomeId: String) {
        val id = incomeId.toIntOrNull() ?: return
        incomeDao.deleteById(id, getCurrentUserId())
        checkLevelUp()
    }

    // ── Categories ────────────────────────────────────────────────

    suspend fun getCustomCategories(): List<CustomCategory> =
        customCategoryDao.getAll(getCurrentUserId()).map {
            CustomCategory(it.id.toString(), it.name, it.icon, it.color, it.budgetLimit)
        }

    suspend fun addCustomCategory(name: String, icon: String, color: String, budgetLimit: Double) {
        customCategoryDao.insert(
            CustomCategoryEntity(
                userId = getCurrentUserId(),
                name = name,
                icon = icon,
                color = color,
                budgetLimit = budgetLimit
            )
        )
    }

    private suspend fun checkLevelUp() {
        val expenses = getExpenses()
        val incomes = getIncomes()
        val totalIncome = incomes.sumOf { it.amount }
        val xp = XPSystem.calculateXP(expenses, totalIncome)
        val currentLevel = XPSystem.getLevelForXP(xp)
        
        val lastSeenLevel = prefs.getInt("last_seen_level", 1)
        if (currentLevel.level > lastSeenLevel) {
            prefs.edit().putInt("last_seen_level", currentLevel.level).apply()
            NotificationHelper.showLevelUpNotification(context, currentLevel)
        } else if (currentLevel.level < lastSeenLevel) {
            // Update last seen if level dropped, so we can notify again on re-entry
            prefs.edit().putInt("last_seen_level", currentLevel.level).apply()
        }
    }
}
