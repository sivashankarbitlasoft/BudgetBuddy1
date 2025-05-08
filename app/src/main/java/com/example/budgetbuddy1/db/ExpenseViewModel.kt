package com.example.budgetbuddy1.db

import androidx.lifecycle.ViewModel
import com.example.budgetbuddy1.budgetdb.BudgetItemEntity
import kotlinx.coroutines.flow.Flow

class ExpenseViewModel(
    private val repository: ExpenseRepository
) : ViewModel() {

    suspend fun insertExpense(expense: Expense){
        repository.insertExpense(expense)
    }

    val getAllExpenses: Flow<List<Expense>> = repository.allExpenses


}