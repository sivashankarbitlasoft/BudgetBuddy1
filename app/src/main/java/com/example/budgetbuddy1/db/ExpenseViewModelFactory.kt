package com.example.budgetbuddy1.db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ExpenseViewModelFactory(
    private val repository: ExpenseRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ExpenseViewModel::class.java)){
            return ExpenseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}