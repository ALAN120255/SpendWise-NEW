package com.example.spendwise

import android.app.AlertDialog
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
import com.google.android.material.textfield.TextInputEditText
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private lateinit var repo: AppRepository
    private var selectedImageUri: Uri? = null
    private var cameraUri: Uri? = null
    private lateinit var ivPic: CircleImageView

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { selectedImageUri = it; Glide.with(this).load(it).circleCrop().into(ivPic) }
    }

    private val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && cameraUri != null) {
            selectedImageUri = cameraUri
            Glide.with(this).load(cameraUri).circleCrop().into(ivPic)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.title = "My Profile"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        repo = AppRepository(this)

        ivPic          = findViewById(R.id.iv_profile_pic)
        val tvName     = findViewById<TextView>(R.id.tv_display_name)
        val tvGender   = findViewById<TextView>(R.id.tv_display_gender)
        val tvEmail    = findViewById<TextView>(R.id.tv_display_email)
        val etFirst    = findViewById<TextInputEditText>(R.id.et_first_name)
        val etLast     = findViewById<TextInputEditText>(R.id.et_last_name)
        val etNewEmail = findViewById<TextInputEditText>(R.id.et_new_email)
        val etNewPass  = findViewById<TextInputEditText>(R.id.et_new_password)
        val btnPhoto   = findViewById<Button>(R.id.btn_change_photo)
        val btnTake    = findViewById<Button>(R.id.btn_take_photo)
        val btnUpdate  = findViewById<Button>(R.id.btn_update_profile)
        val btnUpdEmail= findViewById<Button>(R.id.btn_update_email)
        val btnUpdPass = findViewById<Button>(R.id.btn_update_password)
        val progress   = findViewById<ProgressBar>(R.id.progress_bar)

        lifecycleScope.launch {
            try {
                val profile = repo.getUserProfile()
                tvName.text   = profile.fullName().ifBlank { "—" }
                tvGender.text = profile.gender.ifBlank { "—" }
                tvEmail.text  = profile.email.ifBlank { "—" }
                etFirst.setText(profile.firstName)
                etLast.setText(profile.lastName)
                if (profile.profileImageUrl.isNotEmpty()) {
                    Glide.with(this@ProfileActivity).load(profile.profileImageUrl)
                        .circleCrop().placeholder(R.drawable.ic_default_avatar).into(ivPic)
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProfileActivity, "Failed to load profile", Toast.LENGTH_SHORT).show()
            }
        }

        btnPhoto.setOnClickListener { pickImage.launch("image/*") }
        btnTake.setOnClickListener { 
            cameraUri = CameraHelper.createImageUri(this)
            takePhoto.launch(cameraUri!!)
        }

        btnUpdate.setOnClickListener {
            val fn = etFirst.text.toString().trim()
            val ln = etLast.text.toString().trim()
            if (fn.isEmpty()) { Toast.makeText(this, "Enter first name", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
            progress.visibility = View.VISIBLE
            lifecycleScope.launch {
                val profile = repo.getUserProfile()
                val photoUrl = selectedImageUri?.toString() ?: profile.profileImageUrl
                repo.updateProfile(profile.copy(firstName = fn, lastName = ln, profileImageUrl = photoUrl))
                progress.visibility = View.GONE
                tvName.text = "$fn $ln".trim()
                Toast.makeText(this@ProfileActivity, "Profile updated!", Toast.LENGTH_SHORT).show()
            }
        }

        btnUpdEmail.setOnClickListener {
            val email = etNewEmail.text.toString().trim()
            if (email.isEmpty()) { Toast.makeText(this, "Enter new email", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
            progress.visibility = View.VISIBLE
            lifecycleScope.launch {
                val profile = repo.getUserProfile()
                repo.updateProfile(profile.copy(email = email))
                progress.visibility = View.GONE
                tvEmail.text = email
                Toast.makeText(this@ProfileActivity, "Email updated!", Toast.LENGTH_SHORT).show()
            }
        }

        btnUpdPass.setOnClickListener {
            val pass = etNewPass.text.toString().trim()
            if (pass.length < 6) { Toast.makeText(this, "Password must be 6+ chars", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
            progress.visibility = View.VISIBLE
            lifecycleScope.launch {
                val profile = repo.getUserProfile()
                repo.updateProfile(profile, newPassword = pass)
                progress.visibility = View.GONE
                Toast.makeText(this@ProfileActivity, "Password updated!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPhotoOptions() {
        AlertDialog.Builder(this)
            .setTitle("Profile Photo")
            .setItems(arrayOf("📷 Take Photo", "🖼️ Choose from Gallery")) { _, which ->
                when (which) {
                    0 -> { cameraUri = CameraHelper.createImageUri(this); takePhoto.launch(cameraUri!!) }
                    1 -> pickImage.launch("image/*")
                }
            }.show()
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
