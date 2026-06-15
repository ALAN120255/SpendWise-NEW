package com.example.spendwise

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.spendwise.repositories.AppRepository
import com.example.spendwise.utils.CameraHelper
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var repo: AppRepository
    private var selectedImageUri: Uri? = null
    private var cameraUri: Uri? = null
    private lateinit var ivPhoto: CircleImageView

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { selectedImageUri = it; Glide.with(this).load(it).circleCrop().into(ivPhoto) }
    }

    private val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && cameraUri != null) {
            selectedImageUri = cameraUri
            Glide.with(this).load(cameraUri).circleCrop().into(ivPhoto)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        repo = AppRepository(this)

        val etFirst    = findViewById<TextInputEditText>(R.id.et_first_name)
        val etLast     = findViewById<TextInputEditText>(R.id.et_last_name)
        val etEmail    = findViewById<TextInputEditText>(R.id.et_email)
        val etPassword = findViewById<TextInputEditText>(R.id.et_password)
        val chipGroup  = findViewById<ChipGroup>(R.id.chip_group_gender)
        ivPhoto        = findViewById(R.id.iv_profile_pic)
        val btnPhoto   = findViewById<Button>(R.id.btn_pick_photo)
        val btnRegister= findViewById<Button>(R.id.btn_register)
        val tvLogin    = findViewById<TextView>(R.id.tv_login)
        val progress   = findViewById<ProgressBar>(R.id.progress_bar)

        btnPhoto.setOnClickListener { showPhotoOptions() }

        btnRegister.setOnClickListener {
            val firstName = etFirst.text.toString().trim()
            val lastName  = etLast.text.toString().trim()
            val email     = etEmail.text.toString().trim()
            val password  = etPassword.text.toString().trim()
            val gender    = when (chipGroup.checkedChipId) {
                R.id.chip_male   -> "Male"
                R.id.chip_female -> "Female"
                else             -> "Prefer not to say"
            }
            if (firstName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 8 || password.length > 12) {
                Toast.makeText(this, "Password must be 8-12 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            progress.visibility = View.VISIBLE
            btnRegister.isEnabled = false
            lifecycleScope.launch {
                val result = repo.register(firstName, lastName, gender, email, password, selectedImageUri?.toString() ?: "")
                progress.visibility = View.GONE
                btnRegister.isEnabled = true
                result.onSuccess {
                    Toast.makeText(this@RegisterActivity, "Account created!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                    finish()
                }.onFailure {
                    Toast.makeText(this@RegisterActivity, it.message ?: "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        tvLogin.setOnClickListener { finish() }
    }

    private fun showPhotoOptions() {
        // Register screen: gallery upload only
        pickImage.launch("image/*")
    }
}
