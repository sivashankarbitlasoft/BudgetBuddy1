package com.example.budgetbuddy1.budgetdb

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow


class TransactionsViewModel(
    private val repository: BudgetRepository
) : ViewModel() {

    val budgetItems: Flow<List<BudgetItemEntity>> = repository.getBudgetItems()
}

