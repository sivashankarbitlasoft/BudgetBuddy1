package com.example.budgetbuddy1.budgetdb

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetItemDao {
    @Query("SELECT * FROM budget_items")
    fun getAllBudgetItems(): Flow<List<BudgetItemEntity>>
}
