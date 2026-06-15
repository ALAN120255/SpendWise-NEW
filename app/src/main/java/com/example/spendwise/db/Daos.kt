package com.example.spendwise.db

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun findById(id: Int): UserEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity): Long

    @Update
    suspend fun update(user: UserEntity)

    @Query("UPDATE users SET monthlyBudget = :budget WHERE id = :userId")
    suspend fun updateBudget(userId: Int, budget: Double)
}

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses WHERE userId = :userId ORDER BY date DESC")
    suspend fun getAll(userId: Int): List<ExpenseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: ExpenseEntity): Long

    @Query("DELETE FROM expenses WHERE id = :id AND userId = :userId")
    suspend fun deleteById(id: Int, userId: Int)

    @Query("SELECT * FROM expenses WHERE userId = :userId AND date BETWEEN :start AND :end")
    suspend fun getBetweenDates(userId: Int, start: Long, end: Long): List<ExpenseEntity>

    @Query("SELECT SUM(amount) FROM expenses WHERE userId = :userId")
    suspend fun getTotal(userId: Int): Double?
}

@Dao
interface IncomeDao {
    @Query("SELECT * FROM incomes WHERE userId = :userId ORDER BY date DESC")
    suspend fun getAll(userId: Int): List<IncomeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(income: IncomeEntity): Long

    @Query("DELETE FROM incomes WHERE id = :id AND userId = :userId")
    suspend fun deleteById(id: Int, userId: Int)

    @Query("SELECT SUM(amount) FROM incomes WHERE userId = :userId")
    suspend fun getTotal(userId: Int): Double?
}

@Dao
interface CustomCategoryDao {
    @Query("SELECT * FROM custom_categories WHERE userId = :userId")
    suspend fun getAll(userId: Int): List<CustomCategoryEntity>

    @Insert
    suspend fun insert(category: CustomCategoryEntity): Long

    @Query("DELETE FROM custom_categories WHERE userId = :userId AND name = :name")
    suspend fun deleteByName(userId: Int, name: String)
}
