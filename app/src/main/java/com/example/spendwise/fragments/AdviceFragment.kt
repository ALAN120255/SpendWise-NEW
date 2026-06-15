package com.example.spendwise.fragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.spendwise.R
import com.example.spendwise.repositories.AppRepository
import com.example.spendwise.repositories.CurrencyRepository
import kotlinx.coroutines.launch

class AdviceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_advice, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // We handle data loading in onResume to ensure it's fresh every time the tab is selected
    }

    override fun onResume() {
        super.onResume()
        val root = view ?: return
        val context = context ?: return
        
        val repo = AppRepository(context)
        val sym  = CurrencyRepository(context).getCurrencySymbol()
        
        loadData(
            repo, sym,
            root.findViewById(R.id.tv_advice_income),
            root.findViewById(R.id.tv_advice_spent),
            root.findViewById(R.id.tv_advice_remaining),
            root.findViewById(R.id.progress_advice_budget),
            root.findViewById(R.id.tv_advice_pct),
            root.findViewById(R.id.tv_essential_spent),
            root.findViewById(R.id.tv_essential_pct),
            root.findViewById(R.id.ll_advice_cards),
            root.findViewById(R.id.tv_savings_goal),
            root.findViewById(R.id.progress_savings)
        )
    }

    private fun loadData(
        repo: AppRepository,
        sym: String,
        tvIncome: TextView,
        tvSpent: TextView,
        tvRemaining: TextView,
        progressBar: ProgressBar,
        tvPct: TextView,
        tvEssentialSpent: TextView,
        tvEssentialPct: TextView,
        llAdviceCards: LinearLayout,
        tvSavingsGoal: TextView,
        progressSavings: ProgressBar
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val expenses     = repo.getExpenses()
                val incomes      = repo.getIncomes()
                val totalIncome  = incomes.sumOf { it.amount }
                val totalSpent   = expenses.sumOf { it.amount }
                val remaining    = totalIncome - totalSpent
                val spentPct     = if (totalIncome > 0) ((totalSpent / totalIncome) * 100).toInt().coerceIn(0, 100) else 0

                val essentialSpent = expenses.filter { it.isEssential() }.sumOf { it.amount }
                val essentialPct   = if (totalSpent > 0) ((essentialSpent / totalSpent) * 100).toInt().coerceIn(0, 100) else 0

                // Recommended 20% savings goal
                val savingsGoal   = totalIncome * 0.20
                val actualSavings = (remaining).coerceAtLeast(0.0)
                val savingsPct    = if (savingsGoal > 0) ((actualSavings / savingsGoal) * 100).toInt().coerceIn(0, 100) else 0

                tvIncome.text    = "$sym%.2f".format(totalIncome)
                tvSpent.text     = "$sym%.2f".format(totalSpent)
                tvRemaining.text = "$sym%.2f".format(remaining)
                tvPct.text       = "$spentPct% of income spent"

                progressBar.progress = spentPct
                progressBar.progressTintList = android.content.res.ColorStateList.valueOf(
                    when {
                        spentPct >= 100 -> 0xFFE57373.toInt()
                        spentPct >= 90  -> 0xFFFFB74D.toInt()
                        spentPct >= 75  -> 0xFFFFD54F.toInt()
                        else            -> 0xFF81C784.toInt()
                    }
                )

                tvEssentialSpent.text = "$sym%.2f on essentials".format(essentialSpent)
                tvEssentialPct.text   = "$essentialPct% of spending on needs"

                // FIXED: Escaped % in "20% of income" to "20%% of income" to prevent format crash
                tvSavingsGoal.text = "Goal: $sym%.2f saved (20%% of income) — $sym%.2f saved".format(savingsGoal, actualSavings)
                progressSavings.progress = savingsPct

                // Build advice cards
                llAdviceCards.removeAllViews()
                val adviceList = generateAdvice(sym, totalIncome, totalSpent, remaining, essentialSpent, essentialPct, expenses)
                adviceList.forEach { advice ->
                    addAdviceCard(llAdviceCards, advice)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private data class AdviceCard(
        val emoji: String,
        val title: String,
        val body: String,
        val isWarning: Boolean = false
    )

    private fun generateAdvice(
        sym: String,
        income: Double,
        spent: Double,
        remaining: Double,
        essentialSpent: Double,
        essentialPct: Int,
        expenses: List<com.example.spendwise.Expense>
    ): List<AdviceCard> {
        val advice = mutableListOf<AdviceCard>()

        if (income <= 0) {
            advice.add(AdviceCard("💰", "Add Your Income First",
                "Log your salary or income in the Transactions tab so SpendWise can give you personalised advice based on what you actually earn.", false))
            return advice
        }

        // 50/30/20 rule suggestion
        val needs    = income * 0.50
        val wants    = income * 0.30
        val savings  = income * 0.20
        advice.add(AdviceCard("📐", "Your Recommended 50/30/20 Split",
            "Based on your income of $sym%.2f:\n• 🛒 Needs (50%%): $sym%.2f\n• 🎉 Wants (30%%): $sym%.2f\n• 💰 Savings (20%%): $sym%.2f\n\nEssentials include: Food, Rent, Transport, Health, Utilities, Education.".format(income, needs, wants, savings), false))

        // Essential spend health
        when {
            essentialPct >= 50 ->
                advice.add(AdviceCard("✅", "Great Essentials Ratio",
                    "You're spending $essentialPct% of your money on basic needs — that's a healthy priority. Keep it up!", false))
            essentialPct in 30..49 ->
                advice.add(AdviceCard("⚠️", "Boost Essential Spending",
                    "Only $essentialPct% of your spending goes to essentials (needs 50%+). Consider reallocating some discretionary spend to needs like food, health, and transport.", true))
            else ->
                advice.add(AdviceCard("🚨", "Low Essentials Spending",
                    "Only $essentialPct% of your money is going to essentials. Make sure your core needs — rent, food, health, and transport — are fully covered before spending on wants.", true))
        }

        // Category-specific tips
        val categorySpend = expenses.groupBy { it.category.lowercase() }.mapValues { it.value.sumOf { e -> e.amount } }
        val foodSpent     = categorySpend["food"] ?: 0.0
        val rentSpent     = categorySpend["rent"] ?: 0.0
        val healthSpent   = categorySpend["health"] ?: 0.0

        if (foodSpent == 0.0 && income > 0) {
            // FIXED: Escaped % in "15% of your income"
            advice.add(AdviceCard("🍔", "Track Your Food Budget",
                "No food expenses logged yet. Food is a core need — aim to spend no more than 15%% of your income ($sym%.2f) on food each month.".format(income * 0.15), true))
        } else if (foodSpent > income * 0.25) {
            advice.add(AdviceCard("🍔", "Food Spend Is High",
                "You've spent $sym%.2f on food — that's %.0f%% of income. Try meal-prepping, buying in bulk, or choosing store-brand products to cut this down to around 15%%.".format(foodSpent, (foodSpent / income) * 100), true))
        }

        if (rentSpent > income * 0.35) {
            advice.add(AdviceCard("🏠", "High Rent Alert",
                "Your rent ($sym%.2f) exceeds 35%% of income. Financial advisors recommend keeping housing under 30%%. Consider whether there are ways to reduce housing costs.".format(rentSpent), true))
        }

        if (healthSpent == 0.0 && income > 0) {
            // FIXED: Escaped % in "5% allocation"
            advice.add(AdviceCard("💊", "Don't Skip Health",
                "No health expenses logged. Even a small monthly allocation ($sym%.2f) toward health — check-ups, medicine, vitamins — is a smart investment.".format(income * 0.05), false))
        }

        // Overspending warning
        if (spent > income) {
            advice.add(AdviceCard("🚨", "You're Overspending",
                "Your total spending ($sym%.2f) exceeds your income ($sym%.2f) by $sym%.2f. Review your non-essential expenses immediately to get back on track.".format(spent, income, spent - income), true))
        } else if (remaining < income * 0.10) {
            advice.add(AdviceCard("⚠️", "Very Little Left",
                "You only have $sym%.2f left (%.0f%% of income). Aim to always keep at least 20%% of income as savings for emergencies.".format(remaining, (remaining / income) * 100), true))
        } else {
            advice.add(AdviceCard("🌟", "You Have Breathing Room",
                "You have $sym%.2f remaining (%.0f%% of income). Consider putting at least half of that into an emergency fund or savings account.".format(remaining, (remaining / income) * 100), false))
        }

        // Savings tip
        advice.add(AdviceCard("🏦", "Build an Emergency Fund",
            "Aim to save 3–6 months of essential expenses ($sym%.2f–$sym%.2f) in an accessible savings account. Start small — even $sym%.2f/month adds up quickly.".format(essentialSpent * 3, essentialSpent * 6, income * 0.05), false))

        return advice
    }

    private fun addAdviceCard(
        container: LinearLayout,
        advice: AdviceCard
    ) {
        val ctx = container.context
        val cardView = androidx.cardview.widget.CardView(ctx).apply {
            radius = 32f // Rounded corners for a modern feel
            cardElevation = 4f
            setCardBackgroundColor(
                if (advice.isWarning) 0xFFFFF1F0.toInt() else 0xFFF0F9F0.toInt()
            )
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { bottomMargin = 24 }
        }

        val innerLayout = LinearLayout(ctx).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 40, 40, 40)
        }

        val headerRow = LinearLayout(ctx).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = android.view.Gravity.CENTER_VERTICAL
        }

        val emojiTv = TextView(ctx).apply {
            text = advice.emoji
            textSize = 24f
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { marginEnd = 16 }
        }

        val titleTv = TextView(ctx).apply {
            text = advice.title
            textSize = 16f
            setTypeface(null, android.graphics.Typeface.BOLD)
            setTextColor(if (advice.isWarning) 0xFFD32F2F.toInt() else 0xFF388E3C.toInt())
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        headerRow.addView(emojiTv)
        headerRow.addView(titleTv)

        val bodyTv = TextView(ctx).apply {
            text = advice.body
            textSize = 14f
            setTextColor(0xFF444444.toInt())
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = 8 }
        }

        innerLayout.addView(headerRow)
        innerLayout.addView(bodyTv)
        cardView.addView(innerLayout)
        container.addView(cardView)
    }
}
