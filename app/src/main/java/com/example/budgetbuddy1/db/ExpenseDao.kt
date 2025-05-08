package com.example.budgetbuddy1.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Query(value = "select * from expences order by date desc")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("select sum(amount) from expences")
    fun getTotalSpent(): LiveData<Int>
}