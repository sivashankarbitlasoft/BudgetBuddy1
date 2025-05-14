package com.example.budgetbuddy1.dbbudget

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow

class BudgetViewModel(
    private val repository: BudgetRepository
) : ViewModel() {
    suspend fun insertBudget(budget: Budget){
        repository.insertBudget(budget)
    }
    val getBudget: Flow<List<Budget>> = repository.getBudget

    suspend fun updateBudget(amount: Double) {
        repository.insertOrUpdateBudget(amount)
    }

}