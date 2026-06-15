package com.example.spendwise.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.spendwise.*
import com.example.spendwise.repositories.AppRepository
import com.example.spendwise.repositories.CurrencyRepository
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.launch
import java.util.*

class CategoriesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_categories, container, false)

    override fun onResume() {
        super.onResume()
        val root = view ?: return
        loadCategories(root)
    }

    private fun loadCategories(root: View) {
        val repo = AppRepository(requireContext())
        val sym  = CurrencyRepository(requireContext()).getCurrencySymbol()
        val spinnerPeriod  = root.findViewById<Spinner>(R.id.spinner_cat_period)
        val llCategories   = root.findViewById<LinearLayout>(R.id.ll_categories_list)
        val tvCatTotal     = root.findViewById<TextView>(R.id.tv_cat_total)
        val btnAddCat      = root.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fab_add_category)

        val periods = listOf("All time", "This month", "Last 3 months", "This week")
        spinnerPeriod.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, periods)

        fun refresh(periodPos: Int) {
            viewLifecycleOwner.lifecycleScope.launch {
                val now = System.currentTimeMillis()
                val start: Long? = when (periodPos) {
                    0 -> null
                    1 -> {
                        val cal = Calendar.getInstance().apply { set(Calendar.DAY_OF_MONTH, 1); set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0) }
                        cal.timeInMillis
                    }
                    2 -> now - 90L * 86_400_000
                    3 -> {
                        val cal = Calendar.getInstance().apply { set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0) }
                        cal.timeInMillis
                    }
                    else -> null
                }
                val expenses = if (start != null) repo.getExpensesBetween(start, now) else repo.getExpenses()
                val customCats = repo.getCustomCategories()

                val catSummaries = expenses.groupBy { it.category }.map { (cat, list) ->
                    val custom = customCats.firstOrNull { it.name.equals(cat, true) }
                    CategorySummary(
                        name = cat,
                        icon = list.first().getCategoryIcon(),
                        totalSpent = list.sumOf { it.amount },
                        transactionCount = list.size,
                        color = custom?.color ?: "#E57373",
                        budgetLimit = custom?.budgetLimit ?: 0.0
                    )
                }.sortedByDescending { it.totalSpent }

                val grandTotal = catSummaries.sumOf { it.totalSpent }
                tvCatTotal.text = "Total spent: $sym%.2f".format(grandTotal)

                val ctx = context ?: return@launch
                llCategories.removeAllViews()
                if (catSummaries.isEmpty()) {
                    val tv = TextView(ctx).apply {
                        text = "No expenses found for this period"
                        textSize = 14f; setTextColor(0xFF757575.toInt()); gravity = android.view.Gravity.CENTER; setPadding(0, 40, 0, 40)
                    }
                    llCategories.addView(tv)
                    return@launch
                }

                catSummaries.forEach { summary ->
                    val pct = if (grandTotal > 0) ((summary.totalSpent / grandTotal) * 100).toInt() else 0
                    addCategoryCard(llCategories, summary, pct, sym, expenses)
                }
            }
        }

        spinnerPeriod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) { refresh(pos) }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }
        spinnerPeriod.setSelection(1)  // default: this month

        btnAddCat.setOnClickListener {
            showAddCategoryDialog(repo) { refresh(spinnerPeriod.selectedItemPosition) }
        }
    }

    private fun addCategoryCard(container: LinearLayout, summary: CategorySummary, pct: Int, sym: String, expenses: List<Expense>) {
        val ctx = container.context
        val card = androidx.cardview.widget.CardView(ctx).apply {
            radius = 24f; cardElevation = 4f
            setCardBackgroundColor(if (summary.isOverBudget()) 0xFFFFF1F0.toInt() else 0xFFFFFFFF.toInt())
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { bottomMargin = 12 }
        }
        val inner = LinearLayout(ctx).apply { orientation = LinearLayout.VERTICAL; setPadding(24, 24, 24, 24) }

        // Row 1: icon + name + amount
        val row1 = LinearLayout(ctx).apply { orientation = LinearLayout.HORIZONTAL; gravity = android.view.Gravity.CENTER_VERTICAL }
        row1.addView(TextView(ctx).apply {
            text = summary.icon; textSize = 26f
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { marginEnd = 12 }
        })
        val nameCol = LinearLayout(ctx).apply { orientation = LinearLayout.VERTICAL; layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f) }
        nameCol.addView(TextView(ctx).apply {
            text = summary.name; textSize = 15f
            setTypeface(null, android.graphics.Typeface.BOLD); setTextColor(0xFF212121.toInt())
        })
        nameCol.addView(TextView(ctx).apply {
            text = "${summary.transactionCount} transaction${if (summary.transactionCount != 1) "s" else ""} • $pct% of total"
            textSize = 12f; setTextColor(0xFF757575.toInt())
        })
        row1.addView(nameCol)
        val amtCol = LinearLayout(ctx).apply { orientation = LinearLayout.VERTICAL; gravity = android.view.Gravity.END }
        amtCol.addView(TextView(ctx).apply {
            text = "$sym%.2f".format(summary.totalSpent); textSize = 16f
            setTypeface(null, android.graphics.Typeface.BOLD)
            setTextColor(if (summary.isOverBudget()) 0xFFE53935.toInt() else 0xFF212121.toInt())
        })
        if (summary.budgetLimit > 0) amtCol.addView(TextView(ctx).apply { text = "Limit: $sym%.2f".format(summary.budgetLimit); textSize = 11f; setTextColor(0xFF9E9E9E.toInt()) })
        row1.addView(amtCol)
        inner.addView(row1)

        // Progress bar (% of total spending)
        val pb = ProgressBar(ctx, null, android.R.attr.progressBarStyleHorizontal).apply {
            max = 100; progress = pct
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 12).apply { topMargin = 12; bottomMargin = 4 }
            progressTintList = android.content.res.ColorStateList.valueOf(
                if (summary.isOverBudget()) 0xFFE53935.toInt() else 0xFFD32F2F.toInt()
            )
        }
        inner.addView(pb)

        if (summary.isOverBudget()) {
            inner.addView(TextView(ctx).apply { text = "⚠️ Over budget limit!"; textSize = 12f; setTextColor(0xFFE53935.toInt()) })
        }

        card.addView(inner)
        card.setOnClickListener { showCategoryDetail(summary, expenses.filter { it.category.equals(summary.name, true) }, sym) }
        container.addView(card)
    }

    private fun showCategoryDetail(summary: CategorySummary, catExpenses: List<Expense>, sym: String) {
        val ctx = context ?: return
        val dialog = BottomSheetDialog(ctx)
        val scrollView = ScrollView(ctx)
        val layout = LinearLayout(ctx).apply { orientation = LinearLayout.VERTICAL; setPadding(32, 32, 32, 32) }

        layout.addView(TextView(ctx).apply {
            text = "${summary.icon} ${summary.name}"; textSize = 20f
            setTypeface(null, android.graphics.Typeface.BOLD); setTextColor(0xFF212121.toInt())
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { bottomMargin = 4 }
        })
        layout.addView(TextView(ctx).apply {
            text = "Total: $sym%.2f  •  ${catExpenses.size} transactions".format(summary.totalSpent)
            textSize = 14f; setTextColor(0xFF757575.toInt())
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { bottomMargin = 16 }
        })

        val divider = View(ctx).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1).apply { bottomMargin = 16 }
            setBackgroundColor(0xFFEEEEEE.toInt())
        }
        layout.addView(divider)

        val sdf = java.text.SimpleDateFormat("MMM dd, yyyy  HH:mm", Locale.getDefault())
        catExpenses.sortedByDescending { it.date }.forEach { exp ->
            val row = LinearLayout(ctx).apply { orientation = LinearLayout.HORIZONTAL; gravity = android.view.Gravity.CENTER_VERTICAL; setPadding(0, 12, 0, 12) }
            val col = LinearLayout(ctx).apply { orientation = LinearLayout.VERTICAL; layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f) }
            col.addView(TextView(ctx).apply { text = exp.description.ifBlank { exp.category }; textSize = 14f; setTextColor(0xFF212121.toInt()) })
            col.addView(TextView(ctx).apply { text = sdf.format(java.util.Date(exp.date)); textSize = 12f; setTextColor(0xFF9E9E9E.toInt()) })
            row.addView(col)
            row.addView(TextView(ctx).apply { text = "-$sym%.2f".format(exp.amount); textSize = 14f; setTypeface(null, android.graphics.Typeface.BOLD); setTextColor(0xFFE53935.toInt()) })
            layout.addView(row)
            layout.addView(View(ctx).apply { layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1); setBackgroundColor(0xFFEEEEEE.toInt()) })
        }

        scrollView.addView(layout)
        dialog.setContentView(scrollView)
        dialog.show()
    }

    private fun showAddCategoryDialog(repo: AppRepository, onAdded: () -> Unit) {
        val ctx = context ?: return
        val layout = LinearLayout(ctx).apply { orientation = LinearLayout.VERTICAL; setPadding(48, 16, 48, 0) }

        val etName  = android.widget.EditText(ctx).apply { hint = "Category name *"; textSize = 15f; layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { bottomMargin = 12 } }
        val etLimit = android.widget.EditText(ctx).apply { hint = "Budget limit (optional)"; textSize = 15f; inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL; layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { bottomMargin = 12 } }

        val iconRow = LinearLayout(ctx).apply { orientation = LinearLayout.HORIZONTAL; gravity = android.view.Gravity.CENTER_VERTICAL }
        val tvIconHint = TextView(ctx).apply { text = "Icon: "; textSize = 14f; setTextColor(0xFF757575.toInt()) }
        val etIcon = android.widget.EditText(ctx).apply { hint = "💳"; textSize = 20f; layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT); minWidth = 80 }
        iconRow.addView(tvIconHint); iconRow.addView(etIcon)
        iconRow.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { bottomMargin = 12 }

        layout.addView(etName); layout.addView(iconRow); layout.addView(etLimit)

        AlertDialog.Builder(ctx)
            .setTitle("➕ Add Category")
            .setView(layout)
            .setPositiveButton("Add") { _, _ ->
                val name = etName.text.toString().trim()
                if (name.isEmpty()) { Toast.makeText(ctx, "Enter category name", Toast.LENGTH_SHORT).show(); return@setPositiveButton }
                viewLifecycleOwner.lifecycleScope.launch {
                    repo.addCustomCategory(name, etIcon.text.toString().trim().ifBlank { "💳" }, "#E57373", etLimit.text.toString().toDoubleOrNull() ?: 0.0)
                    onAdded()
                }
            }
            .setNegativeButton("Cancel", null).show()
    }
}
