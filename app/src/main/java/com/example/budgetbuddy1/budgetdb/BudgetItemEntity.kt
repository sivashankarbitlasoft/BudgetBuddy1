package com.example.budgetbuddy1.budgetdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget_items")
data class BudgetItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amountSpent: Int,
    val totalBudget: Int,
    val date: String
)
