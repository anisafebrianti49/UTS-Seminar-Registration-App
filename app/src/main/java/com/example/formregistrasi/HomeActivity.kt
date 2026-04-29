package com.example.formregistrasi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        bottomNav = findViewById(R.id.bottomNav)

        // Load pertama
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        // Listener BottomNav (FIX)
        bottomNav.setOnItemSelectedListener { item ->

            val currentFragment =
                supportFragmentManager.findFragmentById(R.id.frameContainer)

            // 🚨 JANGAN override kalau lagi di flow
            if (currentFragment is FormSeminarFragment ||
                currentFragment is ResultFragment) {
                return@setOnItemSelectedListener false
            }

            val selectedFragment: Fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_form -> FormSeminarFragment()
                R.id.nav_result -> ResultFragment()
                else -> return@setOnItemSelectedListener false
            }

            loadFragment(selectedFragment)
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameContainer, fragment)
            .commit()
    }
}