package com.example.budgetbuddy1.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Expense::class], version = 1)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var INSTANSE: ExpenseDatabase?=null

        fun getDatabase(context: Context): ExpenseDatabase{
            return INSTANSE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDatabase::class.java,
                    "expense_database"
                ).build().also {
                    INSTANSE=it
                }

            }
        }

    }

}