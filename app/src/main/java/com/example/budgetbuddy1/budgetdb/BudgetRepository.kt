package com.example.budgetbuddy1.budgetdb

import android.content.Context

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BudgetRepository(context: Context) {

    private val dao = BudgetDatabase.getInstance(context).budgetItemDao()

    fun getBudgetItems(): Flow<List<BudgetItemEntity>> {
        return dao.getAllBudgetItems().map { entityList ->
            entityList.map { entity ->
                BudgetItemEntity(
                    title = entity.title,
                    amountSpent = entity.amountSpent,
                    totalBudget = entity.totalBudget,
                    date = entity.date
                )
            }
        }
    }

    // You can also add insert, update, delete functions here as needed
}
