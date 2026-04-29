package com.example.formregistrasi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val inputUsername = findViewById<TextInputEditText>(R.id.inputUsername)
        val inputPassword = findViewById<TextInputEditText>(R.id.inputPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnDaftar = findViewById<Button>(R.id.btnDaftar)
        val txtLupaPassword = findViewById<TextView>(R.id.txtLupaPassword)

        val sharedPref = getSharedPreferences("USER_PREF", Context.MODE_PRIVATE)

        btnLogin.setOnClickListener {
            val username = inputUsername.text.toString().trim()
            val password = inputPassword.text.toString().trim()

            val savedUsername = sharedPref.getString("username_regis", null)
            val savedPassword = sharedPref.getString("password_regis", null)
            val savedEmail = sharedPref.getString("email", "user@email.com")

            if (username.isEmpty()) {
                inputUsername.error = "Username wajib diisi"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                inputPassword.error = "Password wajib diisi"
                return@setOnClickListener
            }

            if (savedUsername == null) {
                Toast.makeText(this, "Belum ada akun, silakan daftar dulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (username != savedUsername) {
                inputUsername.error = "Username salah"
                return@setOnClickListener
            }

            if (password != savedPassword) {
                inputPassword.error = "Password salah"
                return@setOnClickListener
            }

            // Simpan data login
            val editor = sharedPref.edit()
            editor.putString("username", username)
            editor.putString("email", savedEmail)
            editor.apply()

            Toast.makeText(this, "Selamat datang, $username!", Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        btnDaftar.setOnClickListener {
            startActivity(Intent(this, RegistrasiActivity::class.java))
        }

        // FITUR LUPA PASSWORD
        txtLupaPassword.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }

    }
}
