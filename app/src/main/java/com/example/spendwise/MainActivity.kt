package com.example.spendwise

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.spendwise.repositories.AppRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repo = AppRepository(this)
        if (!repo.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish(); return
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottom_navigation)
            .setupWithNavController(navController)

        requestNotificationPermission()
        loadHeader(repo)
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
    }

    private fun loadHeader(repo: AppRepository) {
        val tvName  = findViewById<TextView>(R.id.tv_user_name_header)
        val ivAvatar = findViewById<CircleImageView>(R.id.iv_user_avatar_header)

        val openProfile = android.view.View.OnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        tvName.setOnClickListener(openProfile)
        ivAvatar.setOnClickListener(openProfile)

        lifecycleScope.launch {
            try {
                val profile = repo.getUserProfile()
                val name = profile.firstName.ifBlank { profile.email.substringBefore("@") }
                tvName.text = "Hi there, $name"
                if (profile.profileImageUrl.isNotEmpty()) {
                    Glide.with(this@MainActivity)
                        .load(profile.profileImageUrl)
                        .circleCrop()
                        .placeholder(R.drawable.ic_default_avatar)
                        .into(ivAvatar)
                } else {
                    ivAvatar.setImageResource(R.drawable.ic_default_avatar)
                }
            } catch (e: Exception) {
                tvName.text = "Hi there"
            }
        }
    }
}
