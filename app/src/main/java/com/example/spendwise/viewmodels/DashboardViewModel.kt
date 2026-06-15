package com.example.spendwise.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.spendwise.Expense
import com.example.spendwise.Income
import com.example.spendwise.repositories.AppRepository
import kotlinx.coroutines.launch

class DashboardViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = AppRepository(app.applicationContext)

    private val _expenses = MutableLiveData<List<Expense>>()
    val expenses: LiveData<List<Expense>> = _expenses

    private val _incomes = MutableLiveData<List<Income>>()
    val incomes: LiveData<List<Income>> = _incomes

    fun loadData() {
        viewModelScope.launch {
            try {
                _expenses.value = repo.getExpenses()
                _incomes.value  = repo.getIncomes()
            } catch (e: Exception) { /* ignore */ }
        }
    }

    fun getTotalExpenses() = _expenses.value?.sumOf { it.amount } ?: 0.0
    fun getTotalIncome()   = _incomes.value?.sumOf { it.amount } ?: 0.0
    fun getNetBalance()    = getTotalIncome() - getTotalExpenses()
}
