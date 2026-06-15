package com.example.spendwise

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.spendwise.repositories.AppRepository
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var repo: AppRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        repo = AppRepository(this)

        if (repo.isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish(); return
        }

        val etEmail    = findViewById<TextInputEditText>(R.id.et_email)
        val etPassword = findViewById<TextInputEditText>(R.id.et_password)
        val btnLogin   = findViewById<Button>(R.id.btn_login)
        val tvRegister = findViewById<TextView>(R.id.tv_register)
        val progress   = findViewById<ProgressBar>(R.id.progress_bar)

        // Hide Google Sign-In button — SQLite only
        findViewById<Button>(R.id.btn_google_sign_in).visibility = View.GONE
        findViewById<TextView>(R.id.tv_forgot_password).visibility = View.GONE

        btnLogin.setOnClickListener {
            val email    = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            progress.visibility = View.VISIBLE
            btnLogin.isEnabled = false
            lifecycleScope.launch {
                val result = repo.login(email, password)
                progress.visibility = View.GONE
                btnLogin.isEnabled = true
                result.onSuccess {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }.onFailure {
                    Toast.makeText(this@LoginActivity, it.message ?: "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
