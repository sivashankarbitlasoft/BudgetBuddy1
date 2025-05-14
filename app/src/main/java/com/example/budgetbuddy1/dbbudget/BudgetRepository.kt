package com.example.budgetbuddy1.dbbudget

import android.content.Context

class BudgetRepository(context: Context) {
    private val dao = BudgetDatabase.getDatabase(context).budgetDao()

    val getBudget = dao.getBudget()


    suspend fun insertBudget(budget: Budget){
        dao.insertBudget(budget)
    }

    suspend fun insertOrUpdateBudget(amount: Double){
        val budget = Budget(id = 1, budget = amount)
        dao.insertBudget(budget)
    }

}