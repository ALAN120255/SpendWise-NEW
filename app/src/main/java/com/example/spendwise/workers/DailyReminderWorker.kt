package com.example.spendwise.workers

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.spendwise.MainActivity
import com.example.spendwise.R
import com.example.spendwise.repositories.AppRepository
import com.example.spendwise.repositories.CurrencyRepository

class DailyReminderWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        val repo = AppRepository(applicationContext)
        val sym  = CurrencyRepository(applicationContext).getCurrencySymbol()

        if (!repo.isLoggedIn()) return Result.success()

        // Ensure notification channel exists
        createNotificationChannel()

        val expenses     = repo.getExpenses()
        val incomes      = repo.getIncomes()
        val budget       = repo.getBudget()
        val totalSpent   = expenses.sumOf { it.amount }
        val totalIncome  = incomes.sumOf { it.amount }

        val message = when {
            budget > 0 && totalSpent > budget ->
                "⚠️ You're over budget! Spent $sym%.2f of $sym%.2f".format(totalSpent, budget)
            budget > 0 ->
                "💰 Today's check-in: $sym%.2f/$sym%.2f budget used (%.0f%%)".format(totalSpent, budget, (totalSpent / budget) * 100)
            totalIncome > 0 ->
                "📊 Daily reminder: You've spent $sym%.2f so far this period.".format(totalSpent)
            else ->
                "📝 Don't forget to log today's expenses in SpendWise!"
        }

        val intent = PendingIntent.getActivity(
            applicationContext, 0,
            Intent(applicationContext, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, "spendwise_reminder")
            .setSmallIcon(R.drawable.ic_budget)
            .setContentTitle("SpendWise Daily Reminder")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setContentIntent(intent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        applicationContext.getSystemService(NotificationManager::class.java)
            .notify(1001, notification)

        return Result.success()
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                "spendwise_reminder",
                "Daily Reminder",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = "Daily spending reminder notifications" }
            applicationContext.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }
}
