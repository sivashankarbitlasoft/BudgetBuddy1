package com.example.budgetbuddy1.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expences")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val amount: Double,
    val date: Long
)