package com.example.budgetbuddy1.dbbudget

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Budget::class], version = 1)
abstract class BudgetDatabase : RoomDatabase(){
    abstract fun budgetDao(): BudgetDao

    companion object {
        @Volatile
        private var INSTANSE: BudgetDatabase?=null

        fun getDatabase(context: Context): BudgetDatabase{
             return INSTANSE ?: synchronized(this) {
                 Room.databaseBuilder(
                     context.applicationContext,
                     BudgetDatabase::class.java,
                     "budget_database"
                 ).build().also {
                     INSTANSE=it
                 }
             }
        }
    }
}