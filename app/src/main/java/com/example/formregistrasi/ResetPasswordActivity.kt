package com.example.formregistrasi

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import android.widget.TextView


class ResetPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        val inputUsername = findViewById<TextInputEditText>(R.id.inputUsernameReset)
        val inputPasswordBaru = findViewById<TextInputEditText>(R.id.inputPasswordBaru)
        val inputKonfirmasi = findViewById<TextInputEditText>(R.id.inputKonfirmasiPassword)
        val btnReset = findViewById<Button>(R.id.btnResetPassword)
        val txtBackLogin = findViewById<TextView>(R.id.txtBackLogin)

        txtBackLogin.setOnClickListener {
            finish()
        }


        val sharedPref = getSharedPreferences("USER_PREF", Context.MODE_PRIVATE)

        btnReset.setOnClickListener {

            val username = inputUsername.text.toString().trim()
            val passwordBaru = inputPasswordBaru.text.toString().trim()
            val konfirmasi = inputKonfirmasi.text.toString().trim()

            val savedUsername = sharedPref.getString("username_regis", null)

            // VALIDASI INPUT
            if (username.isEmpty()) {
                inputUsername.error = "Username wajib diisi"
                return@setOnClickListener
            }

            if (passwordBaru.isEmpty()) {
                inputPasswordBaru.error = "Password baru wajib diisi"
                return@setOnClickListener
            }

            if (konfirmasi.isEmpty()) {
                inputKonfirmasi.error = "Konfirmasi password wajib diisi"
                return@setOnClickListener
            }

            if (savedUsername == null) {
                Toast.makeText(this, "Belum ada akun terdaftar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (username != savedUsername) {
                inputUsername.error = "Username tidak ditemukan"
                return@setOnClickListener
            }

            if (passwordBaru.length < 6) {
                inputPasswordBaru.error = "Password minimal 6 karakter"
                return@setOnClickListener
            }

            if (passwordBaru != konfirmasi) {
                inputKonfirmasi.error = "Password tidak cocok"
                return@setOnClickListener
            }

            // SIMPAN PASSWORD BARU
            val editor = sharedPref.edit()
            editor.putString("password_regis", passwordBaru)
            editor.apply()

            Toast.makeText(this, "Password berhasil direset!", Toast.LENGTH_SHORT).show()

            // Kembali ke Login
            finish()
        }
    }
}
