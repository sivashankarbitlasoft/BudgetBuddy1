package com.example.budgetbuddy1.db

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExpenseRepository(context: Context) {
    private val dao = ExpenseDatabase.getDatabase(context).expenseDao()


   val allExpenses = dao.getAllExpenses()
    val totalSpent = dao.getTotalSpent()

    suspend fun insertExpense(expense: Expense) {
        dao.insertExpense(expense)
    }

    fun getAllExpences(): Flow<List<Expense>>{
        return dao.getAllExpenses().map {  entityList ->
            entityList.map {
                entity ->
                Expense(title = entity.title, amount = entity.amount, date = entity.date, type = entity.type)
            }

        }
    }
}