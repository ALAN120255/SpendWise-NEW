package com.example.spendwise.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.spendwise.LoginActivity
import com.example.spendwise.ProfileActivity
import com.example.spendwise.R
import com.example.spendwise.repositories.AppRepository
import com.example.spendwise.repositories.CurrencyRepository

class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_settings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currencyRepo = CurrencyRepository(requireContext())
        val appRepo      = AppRepository(requireContext())

        val switchDark      = view.findViewById<SwitchCompat>(R.id.switch_dark_mode)
        val spinnerCurrency = view.findViewById<Spinner>(R.id.spinner_currency)
        val btnProfile      = view.findViewById<Button>(R.id.btn_profile)
        val btnExport       = view.findViewById<Button>(R.id.btn_export)
        val btnAddCategory  = view.findViewById<Button>(R.id.btn_add_category)
        val btnLogout       = view.findViewById<Button>(R.id.btn_logout)

        switchDark.isChecked = currencyRepo.isDarkMode()
        switchDark.setOnCheckedChangeListener { _, checked ->
            currencyRepo.setDarkMode(checked)
            AppCompatDelegate.setDefaultNightMode(
                if (checked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        val currencies = listOf("USD","EUR","GBP","ZAR","JPY","CAD","AUD")
        spinnerCurrency.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, currencies)
        spinnerCurrency.setSelection(currencies.indexOf(currencyRepo.getCurrency()).coerceAtLeast(0))
        spinnerCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) { currencyRepo.setCurrency(currencies[pos]) }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }

        btnProfile.setOnClickListener { startActivity(Intent(requireContext(), ProfileActivity::class.java)) }

        btnExport.setOnClickListener { Toast.makeText(context, "CSV export coming soon!", Toast.LENGTH_SHORT).show() }

        btnAddCategory.setOnClickListener {
            val input = EditText(requireContext()).apply { hint = "Category name" }
            AlertDialog.Builder(requireContext())
                .setTitle("Add Custom Category").setView(input)
                .setPositiveButton("Add") { _, _ ->
                    val name = input.text.toString().trim()
                    if (name.isNotEmpty()) Toast.makeText(context, "Category '$name' added!", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null).show()
        }

        btnLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Logout").setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout") { _, _ ->
                    appRepo.logout()
                    startActivity(Intent(requireContext(), LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                }
                .setNegativeButton("Cancel", null).show()
        }
    }
}
