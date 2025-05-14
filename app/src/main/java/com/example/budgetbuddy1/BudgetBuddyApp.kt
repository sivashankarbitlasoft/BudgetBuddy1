package com.example.budgetbuddy1

import android.app.Application
import com.example.budgetbuddy1.db.ExpenseDatabase
import com.example.budgetbuddy1.db.ExpenseRepository
import com.example.budgetbuddy1.dbbudget.BudgetRepository

class BudgetBuddyApp : Application() {

    val database by lazy { ExpenseDatabase.getDatabase(this) }
    val repository by lazy { ExpenseRepository(this) }
    val repo2Budget by lazy { BudgetRepository(this) }
}