package com.example.budgetbuddy1.db

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromExpenseType(type: ExpenseType): String = type.name

    @TypeConverter
    fun toExpenseType(type: String): ExpenseType = ExpenseType.valueOf(type)
}