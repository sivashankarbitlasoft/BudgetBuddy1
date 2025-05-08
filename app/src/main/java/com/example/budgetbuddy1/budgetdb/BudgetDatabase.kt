package com.example.budgetbuddy1.budgetdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BudgetItemEntity::class], version = 1)
abstract class BudgetDatabase : RoomDatabase() {
    abstract fun budgetItemDao(): BudgetItemDao

    companion object {
        @Volatile
        private var INSTANCE: BudgetDatabase? = null

        fun getInstance(context: Context): BudgetDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    BudgetDatabase::class.java,
                    "budget_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}
