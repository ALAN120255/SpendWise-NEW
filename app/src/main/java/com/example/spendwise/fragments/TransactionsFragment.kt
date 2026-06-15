package com.example.spendwise.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spendwise.*
import com.example.spendwise.repositories.AppRepository
import com.example.spendwise.repositories.CurrencyRepository
import com.example.spendwise.utils.CameraHelper
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

class TransactionsFragment : Fragment() {

    private lateinit var repo: AppRepository
    private lateinit var adapter: TransactionAdapter
    private var allItems = listOf<TransactionItem>()

    // Dialog-level state
    private var selectedPhotoUri: Uri? = null
    private var selectedDateMillis = System.currentTimeMillis()
    private var cameraUri: Uri? = null
    private var dialogIvPreview: ImageView? = null
    private var dialogBtnClear: Button? = null

    // ── Camera: opens camera app directly ──────────────────────
    private val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && cameraUri != null) {
            selectedPhotoUri = cameraUri
            showPreview(selectedPhotoUri)
        }
    }

    // ── Gallery picker ─────────────────────────────────────────
    private val pickPhoto = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedPhotoUri = it
            showPreview(selectedPhotoUri)
        }
    }

    private fun showPreview(uri: Uri?) {
        if (uri == null) {
            dialogIvPreview?.visibility = View.GONE
            dialogBtnClear?.visibility  = View.GONE
        } else {
            dialogIvPreview?.let { iv ->
                iv.visibility = View.VISIBLE
                iv.setImageURI(uri)
            }
            dialogBtnClear?.visibility = View.VISIBLE
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_transactions, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repo = AppRepository(requireContext())
        val sym = CurrencyRepository(requireContext()).getCurrencySymbol()

        val rv        = view.findViewById<RecyclerView>(R.id.recycler_transactions)
        val etSearch  = view.findViewById<EditText>(R.id.et_search)
        val chipGroup = view.findViewById<ChipGroup>(R.id.chip_group_filter)
        val fab       = view.findViewById<FloatingActionButton>(R.id.fab_add)
        val btnExport = view.findViewById<Button?>(R.id.btn_export_csv)

        adapter = TransactionAdapter(emptyList(), sym) { item ->
            AlertDialog.Builder(requireContext())
                .setTitle("Delete Transaction")
                .setMessage("Delete ${item.category} — $sym%.2f?".format(item.amount))
                .setPositiveButton("Delete") { _, _ ->
                    lifecycleScope.launch {
                        if (item.isExpense) repo.deleteExpense(item.id)
                        else repo.deleteIncome(item.id)
                        refreshData(chipGroup, etSearch.text.toString())
                    }
                }
                .setNegativeButton("Cancel", null).show()
        }
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
            override fun onTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) { filterList(chipGroup, s.toString()) }
            override fun afterTextChanged(s: Editable?) {}
        })

        chipGroup.setOnCheckedStateChangeListener { _, _ -> filterList(chipGroup, etSearch.text.toString()) }
        fab.setOnClickListener { showAddDialog(sym) }
        btnExport?.setOnClickListener { exportCSV() }
        refreshData(chipGroup, "")
    }

    // ── Data loading: sorted most-recent first ─────────────────
    private fun refreshData(chipGroup: ChipGroup, query: String) {
        lifecycleScope.launch {
            val expenses = repo.getExpenses().map {
                TransactionItem(it.id, it.amount, it.category, it.description, it.date, true, it.getCategoryIcon(), it.photoUrl)
            }
            val incomes = repo.getIncomes().map {
                TransactionItem(it.id, it.amount, it.category, it.description, it.date, false, it.getCategoryIcon(), "")
            }
            allItems = (expenses + incomes).sortedByDescending { it.date }
            filterList(chipGroup, query)
        }
    }

    private fun filterList(chipGroup: ChipGroup, query: String) {
        val id = chipGroup.checkedChipId
        val filtered = allItems.filter { item ->
            val matchType = when (id) {
                R.id.chip_expenses -> item.isExpense
                R.id.chip_income   -> !item.isExpense
                else               -> true
            }
            val matchQuery = query.isEmpty() ||
                item.category.contains(query, true) ||
                item.description.contains(query, true)
            matchType && matchQuery
        }
        adapter.updateItems(filtered)
    }

    // ── Export CSV ─────────────────────────────────────────────
    private fun exportCSV() {
        lifecycleScope.launch {
            try {
                val sb = StringBuilder("Type,Date,Time,Category,Description,Amount\n")
                val dateFmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val timeFmt = SimpleDateFormat("HH:mm",      Locale.getDefault())
                allItems.forEach { item ->
                    val d = Date(item.date)
                    val sign = if (item.isExpense) "-" else "+"
                    val type = if (item.isExpense) "Expense" else "Income"
                    sb.appendLine("$type,${dateFmt.format(d)},${timeFmt.format(d)},${item.category},${item.description},$sign${item.amount}")
                }
                val file = File(requireContext().getExternalFilesDir(null), "spendwise_export.csv")
                FileWriter(file).use { it.write(sb.toString()) }
                Toast.makeText(context, "Saved: ${file.absolutePath}", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Export failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ── Add Transaction Dialog ─────────────────────────────────
    private fun showAddDialog(sym: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_transaction, null)
        val dialog = AlertDialog.Builder(requireContext()).setView(dialogView).create()

        val chipGroupType = dialogView.findViewById<ChipGroup>(R.id.chip_group_type)
        val etAmount      = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_amount)
        val etDesc        = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_description)
        val spinner       = dialogView.findViewById<Spinner>(R.id.spinner_category)
        val btnDate       = dialogView.findViewById<Button>(R.id.btn_select_date)
        val btnCamera     = dialogView.findViewById<Button>(R.id.btn_take_photo)   // 📷 Camera
        val btnGallery    = dialogView.findViewById<Button>(R.id.btn_pick_photo)   // 🖼 Gallery
        val btnClearPhoto = dialogView.findViewById<Button>(R.id.btn_clear_photo)
        val layoutPhoto   = dialogView.findViewById<View>(R.id.layout_photo)
        val ivPreview     = dialogView.findViewById<ImageView>(R.id.iv_expense_photo)
        val btnCancel     = dialogView.findViewById<Button>(R.id.btn_cancel)
        val btnSave       = dialogView.findViewById<Button>(R.id.btn_save)

        // Store refs so callbacks can update the UI
        dialogIvPreview = ivPreview
        dialogBtnClear  = btnClearPhoto
        selectedPhotoUri = null
        cameraUri        = null
        selectedDateMillis = System.currentTimeMillis()
        ivPreview.visibility    = View.GONE
        btnClearPhoto.visibility = View.GONE

        val expCats = listOf("Food","Transport","Shopping","Health","Entertainment","Utilities","Rent","Education","Other")
        val incCats = listOf("Salary","Freelance","Investment","Gift","Rental","Other")

        fun updateSpinner(isExpense: Boolean) {
            spinner.adapter = ArrayAdapter(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                if (isExpense) expCats else incCats)
        }
        updateSpinner(true)

        // Date + time on button
        fun updateDateBtn() {
            btnDate.text = SimpleDateFormat("dd/MM/yyyy  HH:mm", Locale.getDefault())
                .format(Date(selectedDateMillis))
        }
        updateDateBtn()

        btnDate.setOnClickListener {
            val cal = Calendar.getInstance().apply { timeInMillis = selectedDateMillis }
            DatePickerDialog(requireContext(), { _, y, m, d ->
                cal.set(y, m, d)
                TimePickerDialog(requireContext(), { _, h, min ->
                    cal.set(Calendar.HOUR_OF_DAY, h)
                    cal.set(Calendar.MINUTE, min)
                    selectedDateMillis = cal.timeInMillis
                    updateDateBtn()
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        chipGroupType.setOnCheckedStateChangeListener { _, ids ->
            val isExp = ids.contains(R.id.chip_expense)
            layoutPhoto.visibility = if (isExp) View.VISIBLE else View.GONE
            updateSpinner(isExp)
        }

        // ── Camera button ──────────────────────────────────────
        btnCamera.setOnClickListener {
            try {
                cameraUri = CameraHelper.createImageUri(requireContext())
                takePhoto.launch(cameraUri!!)
            } catch (e: Exception) {
                Toast.makeText(context, "Camera unavailable: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // ── Gallery button ─────────────────────────────────────
        btnGallery.setOnClickListener {
            pickPhoto.launch("image/*")
        }

        // ── Clear photo ────────────────────────────────────────
        btnClearPhoto.setOnClickListener {
            selectedPhotoUri = null
            cameraUri        = null
            showPreview(null)
        }

        btnCancel.setOnClickListener {
            dialogIvPreview = null
            dialogBtnClear  = null
            dialog.dismiss()
        }

        btnSave.setOnClickListener {
            val amountStr = etAmount.text.toString().trim()
            if (amountStr.isEmpty()) {
                Toast.makeText(context, "Enter amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val amount = amountStr.toDoubleOrNull() ?: run {
                Toast.makeText(context, "Invalid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val desc     = etDesc.text.toString().trim()
            val category = spinner.selectedItem.toString()
            val isExpense = chipGroupType.checkedChipId == R.id.chip_expense

            lifecycleScope.launch {
                try {
                    if (isExpense) {
                        repo.addExpense(Expense(
                            amount = amount, category = category,
                            description = desc, date = selectedDateMillis,
                            photoUrl = selectedPhotoUri?.toString() ?: ""
                        ))
                    } else {
                        repo.addIncome(Income(
                            amount = amount, category = category,
                            description = desc, date = selectedDateMillis
                        ))
                    }
                    dialogIvPreview = null
                    dialogBtnClear  = null
                    dialog.dismiss()
                    view?.let { refreshData(it.findViewById(R.id.chip_group_filter), "") }
                } catch (e: Exception) {
                    Toast.makeText(context, "Save failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        view?.let { refreshData(it.findViewById(R.id.chip_group_filter), "") }
    }
}
