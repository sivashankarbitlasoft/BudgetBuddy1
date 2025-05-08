package com.example.budgetbuddy1

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.budgetbuddy1.databinding.DialogAddExpenseBinding
import com.example.budgetbuddy1.databinding.FragmentOverviewBinding
import com.example.budgetbuddy1.db.Expense
import com.example.budgetbuddy1.db.ExpenseViewModel
import com.example.budgetbuddy1.db.ExpenseViewModelFactory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class OverviewFragment : Fragment() {
    private lateinit var binding: FragmentOverviewBinding

    private lateinit var viewModel: ExpenseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOverviewBinding.inflate(inflater, container, false)
        val app = requireActivity().application as BudgetBuddyApp
        val factory = ExpenseViewModelFactory(app.repository)
        viewModel = ViewModelProvider(this, factory)[ExpenseViewModel::class.java]
        binding.fabAddExpense.setOnClickListener{
            showAddExpenseDialog()
        }

        return binding.root
    }

    private fun showAddExpenseDialog(){


        val dialogBinding = DialogAddExpenseBinding.inflate(LayoutInflater.from(requireContext()))
        var selectedDate: Long = System.currentTimeMillis()

        dialogBinding.etAddExpenseDate.setOnClickListener{
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                requireContext(),
                {_, year, month, day ->
                    calendar.set(year,month,day)
                    selectedDate = calendar.timeInMillis
                    val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
                    dialogBinding.etAddExpenseDate.setText(formattedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }
        val dialog = AlertDialog.Builder(requireContext()).setView(dialogBinding.root)
            .setTitle("Add Expense")
            .setPositiveButton("Add", null)
            .setNegativeButton("Cancel",null)
            .create()

        dialog.setOnShowListener{
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener{
                val title = dialogBinding.etAddExpenseTitle.text.toString().trim()
                val amount = dialogBinding.etAddExpenseAmount.text.toString().toDoubleOrNull()

                if(title.isEmpty() || amount == null) {
                    Toast.makeText(requireContext(),"Please enter valid data", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val expense = Expense(title = title, amount = amount, date = selectedDate)
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.insertExpense(expense)
                    dialog.dismiss()
                }
            }
        }
        dialog.show()
    }
}