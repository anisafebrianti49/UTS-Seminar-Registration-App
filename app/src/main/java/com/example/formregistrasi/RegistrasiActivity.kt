package com.example.formregistrasi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class RegistrasiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrasi)

        // --- INISIALISASI SEMUA VIEW ---
        val inputNama = findViewById<EditText>(R.id.inputNama)
        val inputEmail = findViewById<EditText>(R.id.inputEmail)
        val inputNoHp = findViewById<EditText>(R.id.inputNoHp)
        val radioGender = findViewById<RadioGroup>(R.id.radioGender)
        val spinnerKota = findViewById<Spinner>(R.id.spinnerKota)
        val inputKotaLain = findViewById<EditText>(R.id.inputKotaLain)

        val cbMembaca = findViewById<CheckBox>(R.id.cbMembaca)
        val cbMusik = findViewById<CheckBox>(R.id.cbMusik)
        val cbOlahraga = findViewById<CheckBox>(R.id.cbOlahraga)
        val cbGaming = findViewById<CheckBox>(R.id.cbGaming)
        val cbTraveling = findViewById<CheckBox>(R.id.cbTraveling)

        val inputUsername = findViewById<EditText>(R.id.inputUsernameDaftar)
        val inputPassword = findViewById<EditText>(R.id.inputPasswordDaftar)
        val inputConfirmPassword = findViewById<EditText>(R.id.inputConfirmPassword)

        val cbSetuju = findViewById<CheckBox>(R.id.cbSetuju)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)

        // SINKRONISASI NAMA FILE PREF DENGAN LOGIN & HOME
        val sharedPref = getSharedPreferences("USER_PREF", Context.MODE_PRIVATE)

        // --- LOGIKA SPINNER KOTA ---
        val kota = arrayOf("Pilih Kota", "Jakarta", "Bandung", "Surabaya", "Yogyakarta", "Malang", "Semarang", "Lainnya")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, kota)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerKota.adapter = adapter

        spinnerKota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (spinnerKota.selectedItem.toString() == "Lainnya") {
                    inputKotaLain.visibility = View.VISIBLE
                } else {
                    inputKotaLain.visibility = View.GONE
                    inputKotaLain.text.clear()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // --- LOGIKA TOMBOL SIMPAN ---
        btnSimpan.setOnClickListener {
            val nama = inputNama.text.toString().trim()
            val email = inputEmail.text.toString().trim()
            val noHp = inputNoHp.text.toString().trim()
            val username = inputUsername.text.toString().trim()
            val password = inputPassword.text.toString().trim()
            val confirmPassword = inputConfirmPassword.text.toString().trim()
            val kotaDipilih = spinnerKota.selectedItem.toString()
            val kotaManual = inputKotaLain.text.toString().trim()

            var jumlahHobi = 0
            if (cbMembaca.isChecked) jumlahHobi++
            if (cbMusik.isChecked) jumlahHobi++
            if (cbOlahraga.isChecked) jumlahHobi++
            if (cbGaming.isChecked) jumlahHobi++
            if (cbTraveling.isChecked) jumlahHobi++

            // ================= VALIDASI LENGKAP =================
            if (nama.isEmpty()) {
                inputNama.error = "Nama wajib diisi"; inputNama.requestFocus(); return@setOnClickListener
            }
            if (email.isEmpty() || !email.contains("@")) {
                inputEmail.error = "Email tidak valid"; inputEmail.requestFocus(); return@setOnClickListener
            }
            if (noHp.isEmpty() || !noHp.startsWith("08") || noHp.length < 10) {
                inputNoHp.error = "Nomor HP tidak valid (min 10 digit, mulai 08)"; inputNoHp.requestFocus(); return@setOnClickListener
            }
            if (radioGender.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Pilih jenis kelamin", Toast.LENGTH_SHORT).show(); return@setOnClickListener
            }
            if (kotaDipilih == "Pilih Kota" || (kotaDipilih == "Lainnya" && kotaManual.isEmpty())) {
                Toast.makeText(this, "Pilih/Isi kota dengan benar", Toast.LENGTH_SHORT).show(); return@setOnClickListener
            }
            if (jumlahHobi < 3) {
                Toast.makeText(this, "Pilih minimal 3 hobi", Toast.LENGTH_SHORT).show(); return@setOnClickListener
            }
            if (username.isEmpty()) {
                inputUsername.error = "Username wajib diisi"; inputUsername.requestFocus(); return@setOnClickListener
            }
            if (password.isEmpty() || password != confirmPassword) {
                inputConfirmPassword.error = "Password tidak sama atau kosong"; inputConfirmPassword.requestFocus(); return@setOnClickListener
            }
            if (!cbSetuju.isChecked) {
                Toast.makeText(this, "Harus menyetujui syarat", Toast.LENGTH_SHORT).show(); return@setOnClickListener
            }

            // ================= DIALOG KONFIRMASI =================
            AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Apakah data sudah benar?")
                .setPositiveButton("Ya") { _, _ ->
                    val editor = sharedPref.edit()

                    // Simpan data untuk kebutuhan Login
                    editor.putString("username_regis", username)
                    editor.putString("password_regis", password)

                    // Simpan data untuk kebutuhan Profile di Home (PENTING)
                    editor.putString("username", username)
                    editor.putString("email", email) // Email sekarang disimpan
                    editor.putString("nama_lengkap", nama)

                    editor.apply()

                    Toast.makeText(this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                .setNegativeButton("Tidak", null)
                .show()
        }
    }
}