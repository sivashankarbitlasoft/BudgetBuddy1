package com.example.budgetbuddy1

import android.app.Application
import com.example.budgetbuddy1.db.ExpenseDatabase
import com.example.budgetbuddy1.db.ExpenseRepository

class BudgetBuddyApp : Application() {

    val database by lazy { ExpenseDatabase.getDatabase(this) }
    val repository by lazy { ExpenseRepository(this) }
}