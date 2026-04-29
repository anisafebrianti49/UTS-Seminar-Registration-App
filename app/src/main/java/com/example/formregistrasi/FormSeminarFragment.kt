package com.example.formregistrasi

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment

class FormSeminarFragment : Fragment(R.layout.fragment_form_seminar) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // =========================
        // VIEW BINDING
        // =========================
        val tvSeminar = view.findViewById<TextView>(R.id.tvSelectedSeminar)
        val inputNama = view.findViewById<EditText>(R.id.inputNama)
        val inputEmail = view.findViewById<EditText>(R.id.inputEmail)
        val inputHP = view.findViewById<EditText>(R.id.inputHP)
        val inputInstansi = view.findViewById<EditText>(R.id.inputInstansi)
        val spinnerStatus = view.findViewById<Spinner>(R.id.spinnerStatus)
        val spinnerSumber = view.findViewById<Spinner>(R.id.spinnerSumber)
        val cbSetuju = view.findViewById<CheckBox>(R.id.cbSetuju)
        val btnSubmit = view.findViewById<Button>(R.id.btnSubmit)

        // =========================
        // AMBIL DATA DARI ARGUMENTS
        // =========================
        val seminar = arguments?.getString("key_seminar") ?: "-"
        val tanggal = arguments?.getString("key_tanggal") ?: "-"
        val jam = arguments?.getString("key_jam") ?: "-"
        val lokasi = arguments?.getString("key_lokasi") ?: "-"
        val kategori = arguments?.getString("key_kategori") ?: "-"

        tvSeminar.text = "$seminar\n$tanggal • $jam\n$lokasi\n$kategori"

        // =========================
        // SPINNER
        // =========================
        val listStatus = arrayOf("-- Pilih Status --", "Mahasiswa", "Dosen", "Umum")
        val listSumber = arrayOf("-- Sumber Info --", "Instagram", "WhatsApp", "Teman")

        setupSpinner(spinnerStatus, listStatus)
        setupSpinner(spinnerSumber, listSumber)

        // =========================
        // SHARED PREF
        // =========================
        val sharedPref = requireActivity()
            .getSharedPreferences("TICKET_PREF", Context.MODE_PRIVATE)

        // =========================
        // SUBMIT
        // =========================
        btnSubmit.setOnClickListener {

            val nama = inputNama.text.toString().trim()
            val email = inputEmail.text.toString().trim()
            val hp = inputHP.text.toString().trim()
            val instansi = inputInstansi.text.toString().trim()
            val status = spinnerStatus.selectedItem?.toString() ?: ""
            val sumber = spinnerSumber.selectedItem?.toString() ?: ""

            // VALIDASI
            if (nama.isEmpty()) {
                inputNama.error = "Nama tidak boleh kosong"
                inputNama.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                inputEmail.error = "Email tidak boleh kosong"
                inputEmail.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                inputEmail.error = "Format email tidak valid"
                inputEmail.requestFocus()
                return@setOnClickListener
            }

            if (hp.length < 10) {
                inputHP.error = "No HP tidak valid"
                inputHP.requestFocus()
                return@setOnClickListener
            }

            if (instansi.isEmpty()) {
                inputInstansi.error = "Instansi wajib diisi"
                inputInstansi.requestFocus()
                return@setOnClickListener
            }

            if (status.startsWith("--")) {
                Toast.makeText(requireContext(), "Pilih status", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (sumber.startsWith("--")) {
                Toast.makeText(requireContext(), "Pilih sumber info", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!cbSetuju.isChecked) {
                Toast.makeText(requireContext(), "Harap centang persetujuan", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // =========================
            // SIMPAN DATA
            // =========================
            with(sharedPref.edit()) {
                clear()
                putString("nama", nama)
                putString("email", email)
                putString("hp", hp)
                putString("instansi", instansi)
                putString("status", status)
                putString("sumber", sumber)
                putString("seminar", seminar)
                putString("tanggal", tanggal)
                putString("jam", jam)
                putString("lokasi", lokasi)
                apply()
            }

            Toast.makeText(requireContext(), "Data valid", Toast.LENGTH_SHORT).show()

            // =========================
            // PINDAH KE RESULT (FIX UTAMA)
            // =========================
            val fragment = ResultFragment().apply {
                arguments = Bundle().apply {
                    putString("key_nama", nama)
                    putString("key_email", email)
                    putString("key_hp", hp)
                    putString("key_instansi", instansi)
                    putString("key_status", status)
                    putString("key_seminar", seminar)
                    putString("key_tanggal", tanggal)
                    putString("key_jam", jam)
                    putString("key_lokasi", lokasi)
                }
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.frameContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupSpinner(spinner: Spinner, data: Array<String>) {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            data
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}