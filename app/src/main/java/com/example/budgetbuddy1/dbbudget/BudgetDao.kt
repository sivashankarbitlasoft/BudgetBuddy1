package com.example.budgetbuddy1.dbbudget

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: Budget)

    @Query("select * from budget")
    fun getBudget(): Flow<List<Budget>>

    @Update
    suspend fun updateBudget(budget: Budget)
}