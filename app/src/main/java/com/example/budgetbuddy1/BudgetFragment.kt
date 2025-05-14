package com.example.budgetbuddy1

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.budgetbuddy1.databinding.DialogAddBudgetBinding
import com.example.budgetbuddy1.databinding.FragmentBudgetBinding
import com.example.budgetbuddy1.dbbudget.Budget
import com.example.budgetbuddy1.dbbudget.BudgetRepository
import com.example.budgetbuddy1.dbbudget.BudgetViewModel
import com.example.budgetbuddy1.dbbudget.BudgetViewModelFactory
import kotlinx.coroutines.launch

class BudgetFragment : Fragment() {
    private lateinit var binding: FragmentBudgetBinding
    private lateinit var viewModel: BudgetViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBudgetBinding.inflate(inflater, container, false)
        val app = requireActivity().application as BudgetBuddyApp
        val factory = BudgetViewModelFactory(app.repo2Budget)
        viewModel = ViewModelProvider(this, factory)[BudgetViewModel::class.java]
        observeBudget()

        binding.btUpdateBudget.setOnClickListener{
            val input = binding.editTextUpdateBudget.text.toString()
            val amount = input.toDoubleOrNull()
            if (amount == null){
                Toast.makeText(requireContext(), "Please enter valid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                viewModel.updateBudget(amount)
                Toast.makeText(requireContext(), "Budget updated successfully", Toast.LENGTH_SHORT).show()
            }
        }
        binding.floatBtAddBudget.setOnClickListener{
           showAddBudgetDialog()
       }
        return binding.root
    }

    private fun observeBudget() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getBudget.collect{ budgets ->
                if(budgets.isNotEmpty()) {
                    val currentBudget = budgets[0]
                    binding.tvCurrentBudget.text = "Current Budget: ₹${currentBudget.budget}"
                } else {
                    binding.tvCurrentBudget.text = "No budget set"
                }
            }
        }
    }


    private fun showAddBudgetDialog(){
        val dialogBinding = DialogAddBudgetBinding.inflate(LayoutInflater.from(requireContext()))
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setTitle("Add Budget")
            .setPositiveButton("Add", null)
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setOnShowListener{
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener{
                val input = dialogBinding.etAddBudgetAmount.text.toString()
                val amount1 = input.toDoubleOrNull()

                if (amount1 == null){
                    Toast.makeText(requireContext(), "Please enter valid amount", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.updateBudget(amount1)
                    Toast.makeText(requireContext(),"Budget Saved", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
        }
        dialog.show()
    }
}