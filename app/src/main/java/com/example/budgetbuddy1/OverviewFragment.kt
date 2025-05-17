package com.example.budgetbuddy1

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.budgetbuddy1.databinding.DialogAddExpenseBinding
import com.example.budgetbuddy1.databinding.FragmentOverviewBinding
import com.example.budgetbuddy1.db.Expense
import com.example.budgetbuddy1.db.ExpenseType
import com.example.budgetbuddy1.db.ExpenseViewModel
import com.example.budgetbuddy1.db.ExpenseViewModelFactory
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class OverviewFragment : Fragment() {
    private lateinit var binding: FragmentOverviewBinding
    private lateinit var viewModel: ExpenseViewModel
    lateinit var pieChart: PieChart


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

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getAllExpenses.collect{ expenseList ->
                setUpPiChart(expenseList)
            }
        }
        binding.idTVHead.setOnClickListener(){
            val intent = Intent(requireContext(),Pichart::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun showAddExpenseDialog(){
        val dialogBinding = DialogAddExpenseBinding.inflate(LayoutInflater.from(requireContext()))
        var selectedDate: Long = System.currentTimeMillis()
        //dropdown
        val categories = ExpenseType.values().map { it.name }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories)
        dialogBinding.etAddExpenseCategory.setAdapter(adapter)

        dialogBinding.etAddExpenseCategory.setOnClickListener {
            dialogBinding.etAddExpenseCategory.showDropDown()
        }
        dialogBinding.etAddExpenseDate.setOnClickListener{
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                requireContext(),
                {_, year, month, day ->
                    calendar.set(year,month,day)
                    selectedDate = calendar.timeInMillis
                    val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
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
                val selectExpenseType = dialogBinding.etAddExpenseCategory.text.toString().trim()

                if(title.isEmpty() || amount == null || selectExpenseType.isEmpty()){
                    Toast.makeText(requireContext(),"Please enter valid data", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val expenseType = try {
                    ExpenseType.valueOf(selectExpenseType)
                }
                catch (e: IllegalArgumentException) {
                    Toast.makeText(requireContext(),"Invalid Category selected", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val expense = Expense(title = title, amount = amount, date = selectedDate, type = expenseType)
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.insertExpense(expense)
                    Toast.makeText(requireContext(),"Expense Added Successfully", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
        }
        dialog.show()
    }

    private fun setUpPiChart(expenses: List<Expense>){
        if (!this::binding.isInitialized || binding.pieChart==null || binding.pieChart.viewPortHandler == null) return
        if(expenses.isEmpty()){
            binding.pieChart.clear()
            binding.pieChart.centerText = "No Expenses"
            binding.pieChart.invalidate()
            return
        }
        val grouped = expenses.groupBy { it.type }
        val entries = grouped.map { (type, expenses) ->
            PieEntry(expenses.sumOf { it.amount}.toFloat(), type.name)
        }



        val colors = grouped.keys.map {
            when(it) {
                ExpenseType.FOOD -> Color.rgb(255,99,132)
                ExpenseType.TRANSPORT -> Color.rgb(54,162,235)
                ExpenseType.ENTERTAINMENT -> Color.rgb(255,206,86)
                ExpenseType.UTILITIES -> Color.rgb(75,192,192)
                ExpenseType.OTHER -> Color.rgb(153,102,255)
            }
        }

        pieChart = binding.pieChart

        // on below line we are setting user percent value,
        // setting description as enabled and offset for pie chart
        pieChart.setUsePercentValues(true)
        pieChart.getDescription().setEnabled(false)
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        // on below line we are setting drag for our pie chart
        pieChart.setDragDecelerationFrictionCoef(0.95f)

        // on below line we are setting hole
        // and hole color for pie chart
        pieChart.setDrawHoleEnabled(true)
        pieChart.setHoleColor(Color.WHITE)

        // on below line we are setting circle color and alpha
        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        // on  below line we are setting hole radius
        pieChart.setHoleRadius(58f)
        pieChart.setTransparentCircleRadius(61f)

        // on below line we are setting center text
        pieChart.setDrawCenterText(true)

        // on below line we are setting
        // rotation for our pie chart
        pieChart.setRotationAngle(0f)

        // enable rotation of the pieChart by touch
        pieChart.setRotationEnabled(true)
        pieChart.setHighlightPerTapEnabled(true)

        // on below line we are setting animation for our pie chart
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        // on below line we are disabling our legend for pie chart
        pieChart.legend.isEnabled = false
        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.setEntryLabelTextSize(12f)


        // on below line we are setting pie data set
        val dataSet = PieDataSet(entries, "Expenses")

        // on below line we are setting icons.
        dataSet.setDrawIcons(false)

        // on below line we are setting slice for pie
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        // add a lot of colors to list


        // on below line we are setting colors.
        dataSet.colors = colors

        // on below line we are setting pie data set
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(15f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.WHITE)
        pieChart.setData(data)

        // undo all highlights
        pieChart.highlightValues(null)

        // loading chart
        pieChart.invalidate()

    }
}