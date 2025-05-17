package com.example.budgetbuddy1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budgetbuddy1.budgetdb.BudgetAdapter
import com.example.budgetbuddy1.databinding.FragmentBudgetBinding
import com.example.budgetbuddy1.databinding.FragmentTransactions1Binding
import com.example.budgetbuddy1.db.BudgetRemaining
import com.example.budgetbuddy1.db.ExpenseViewModel
import com.example.budgetbuddy1.db.ExpenseViewModelFactory
import com.example.budgetbuddy1.dbbudget.BudgetViewModel
import com.example.budgetbuddy1.dbbudget.BudgetViewModelFactory
import kotlinx.coroutines.flow.collectLatest

import kotlinx.coroutines.launch

class TransactionsFragment : Fragment() {

    private var _binding: FragmentTransactions1Binding? = null
    private val binding get() = _binding!!

    private lateinit var budgetBinding: FragmentBudgetBinding

    private lateinit var viewModel: ExpenseViewModel
    private lateinit var budgetViewModel: BudgetViewModel
    private lateinit var adapter: BudgetAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val app = requireActivity().application as BudgetBuddyApp
        val budgetFactory = BudgetViewModelFactory(app.repo2Budget)
        budgetViewModel = ViewModelProvider(this, budgetFactory)[BudgetViewModel::class.java]

        _binding = FragmentTransactions1Binding.inflate(inflater, container, false)
        budgetBinding = FragmentBudgetBinding.inflate(inflater, container, false)
        observeBudget()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val app = requireActivity().application as BudgetBuddyApp

        val expenseFactory = ExpenseViewModelFactory(app.repository)
        viewModel = ViewModelProvider(this, expenseFactory)[ExpenseViewModel::class.java]

        val budgetFactory = BudgetViewModelFactory(app.repo2Budget)
        budgetViewModel = ViewModelProvider(this, budgetFactory)[BudgetViewModel::class.java]

        adapter = BudgetAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        binding.progressBar.visibility = View.VISIBLE

        viewLifecycleOwner.lifecycleScope.launch {
            budgetViewModel.getBudget.collectLatest { budgetList ->
                val totalBudget = budgetList.firstOrNull()?.budget ?: 0.0
                Log.d("TransactionsFragment", "Fetched budget: $totalBudget")
                viewModel.getAllExpenses.collectLatest { expenses ->
                    val sortedExpenses = expenses.sortedBy { it.date }
                    var remaining = totalBudget
                    val displayList = sortedExpenses.map { expense ->
                        val item = BudgetRemaining(expense = expense, remBudget = remaining)
                        remaining -= expense.amount
                        item
                    }
                    adapter.updateList(displayList)
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeBudget() {
        viewLifecycleOwner.lifecycleScope.launch {
            budgetViewModel.getBudget.collect{ budgets ->
                if(budgets.isNotEmpty()) {
                    val currentBudget = budgets[0]
                    binding.tvTotalBudget.text = "₹${currentBudget.budget}"
                } else {
                    binding.tvTotalBudget.text = "₹0.0"
                }
            }
        }
    }
}
