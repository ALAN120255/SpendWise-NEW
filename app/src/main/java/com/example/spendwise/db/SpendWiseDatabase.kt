package com.example.spendwise.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [UserEntity::class, ExpenseEntity::class, IncomeEntity::class, CustomCategoryEntity::class],
    version = 3,
    exportSchema = false
)
abstract class SpendWiseDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun incomeDao(): IncomeDao
    abstract fun customCategoryDao(): CustomCategoryDao

    companion object {
        @Volatile private var INSTANCE: SpendWiseDatabase? = null

        fun getInstance(context: Context): SpendWiseDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    SpendWiseDatabase::class.java,
                    "spendwise.db"
                )
                .fallbackToDestructiveMigration()
                .build()
                .also { INSTANCE = it }
            }
    }
}
