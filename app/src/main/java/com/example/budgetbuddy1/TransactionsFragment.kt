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
import com.example.budgetbuddy1.databinding.FragmentTransactions1Binding
import com.example.budgetbuddy1.db.Expense
import com.example.budgetbuddy1.db.ExpenseRepository
import com.example.budgetbuddy1.db.ExpenseViewModel
import com.example.budgetbuddy1.db.ExpenseViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class TransactionsFragment : Fragment() {

    private var _binding: FragmentTransactions1Binding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ExpenseViewModel
    private lateinit var adapter: BudgetAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactions1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val repository = ExpenseRepository(requireContext())
        val factory = ExpenseViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ExpenseViewModel::class.java]

        Log.d("checkIssue", "onviewcreated")
//        var list = mutableListOf(
//            Expense(1,"Petrol",200.00,21032025)
//        )
        //var list2 = viewModel.getAllExpenses
//        adapter = BudgetAdapter(list)
        adapter = BudgetAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        binding.progressBar.visibility = View.VISIBLE

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getAllExpenses.collectLatest { items ->
                adapter.updateList(items)
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
