package com.example.budgetbuddy1.budgetdb

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TransactionsViewModelFactory(
    private val repository: BudgetRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}