package com.example.budgetbuddy1.budgetdb

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetbuddy1.databinding.ItemBudgetBinding
import com.example.budgetbuddy1.db.Expense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//class BudgetAdapter : ListAdapter<BudgetItemEntity, BudgetAdapter.BudgetViewHolder>(DiffCallback()) {
class BudgetAdapter(
    private var list: List<Expense> = emptyList()
) : RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder>() {

    fun updateList(newList: List<Expense>) {
        list = newList
        notifyDataSetChanged()
    }

    inner class BudgetViewHolder(val binding: ItemBudgetBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBudgetBinding.inflate(inflater, parent, false)
        return BudgetViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun convertMillisToDate(millis: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date(millis))
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        val item = list[position]
        Log.d("checkIssue", item.toString())
        with(holder.binding) {
            titleTextView.text = item.title
            val time = item.date
            val formatedDate = convertMillisToDate(time)
            dateTextView.text = formatedDate.trim()
            amountSpentTextView.text = "Spent: ${item.amount}"
            totalBudgetTextView.text = "Budget: ${25000}"

            progressBar.max = 25000
            progressBar.progress = item.amount.toInt()
        }
    }



    class DiffCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem == newItem
        }
    }
}
