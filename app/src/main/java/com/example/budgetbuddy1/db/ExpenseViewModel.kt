package com.example.budgetbuddy1.db

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExpenseViewModel(
    private val repository: ExpenseRepository
) : ViewModel() {
    suspend fun insertExpense(expense: Expense){
        repository.insertExpense(expense)
    }
    val getAllExpenses: Flow<List<Expense>> = repository.allExpenses

    fun getExpensesWithRemainingBudget(initialBudget: Double): Flow<List<BudgetRemaining>>{
        return repository.getAllExpences().map {
            expenses ->
            var remaining = initialBudget
            expenses.map {
                expense ->
                remaining -= expense.amount
                BudgetRemaining(expense,remaining)
            }
        }
    }
}