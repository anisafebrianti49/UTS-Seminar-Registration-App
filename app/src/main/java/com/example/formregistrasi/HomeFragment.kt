package com.example.formregistrasi

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.formregistrasi.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var currentCategory = "Semua"
    private var currentQuery = ""
    private lateinit var adapter: SeminarAdapter

    private val getContent = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let {
            try {
                requireContext().contentResolver.takePersistableUriPermission(
                    it,
                    android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }

            binding.ivProfile.setImageURI(it)
            saveProfileImage(it.toString())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        setupUI()
        setupRecyclerView()
        setupListeners()
    }

    private fun setupUI() {
        val sharedPref = requireActivity().getSharedPreferences("USER_PREF", Context.MODE_PRIVATE)

        val username = sharedPref.getString("username", "Peserta")
        val email = sharedPref.getString("email", "peserta@email.com")
        val savedImage = sharedPref.getString("profile_image", null)

        binding.tvUserName.text = username
        binding.tvUserEmail.text = email

        if (!savedImage.isNullOrEmpty()) {
            try {
                val uri = savedImage.toUri()
                requireContext().contentResolver.takePersistableUriPermission(
                    uri,
                    android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                binding.ivProfile.setImageURI(uri)
            } catch (e: Exception) {
                binding.ivProfile.setImageResource(R.drawable.ic_profile_placeholder)
            }
        }
    }

    private fun setupListeners() {

        binding.ivProfile.setOnClickListener {
            getContent.launch(arrayOf("image/*"))
        }

        binding.ivSearch.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Cari Seminar")

            val input = EditText(requireContext())
            input.hint = "Ketik nama seminar..."
            input.setPadding(60, 40, 60, 40)
            builder.setView(input)

            builder.setPositiveButton("Cari") { _, _ ->
                currentQuery = input.text.toString().trim()
                filterList(currentQuery, currentCategory)
            }

            builder.setNegativeButton("Reset") { _, _ ->
                currentQuery = ""
                filterList(currentQuery, currentCategory)
            }

            builder.show()
        }

        // ✅ LOGOUT
        binding.btnLogout.setOnClickListener {
            val sharedPref = requireActivity().getSharedPreferences("USER_PREF", Context.MODE_PRIVATE)

            AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Yakin mau keluar?")
                .setPositiveButton("Ya") { _, _ ->

                    // hapus data login
                    sharedPref.edit {
                        remove("username")
                        remove("email")
                    }

                    Toast.makeText(requireContext(), "Berhasil logout", Toast.LENGTH_SHORT).show()

                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                .setNegativeButton("Batal", null)
                .show()
        }

        binding.btnSemua.setOnClickListener {
            currentCategory = "Semua"
            filterList(currentQuery, currentCategory)
        }

        binding.btnTech.setOnClickListener {
            currentCategory = "Tech"
            filterList(currentQuery, currentCategory)
        }

        binding.btnSoftSkills.setOnClickListener {
            currentCategory = "Soft Skills"
            filterList(currentQuery, currentCategory)
        }

        binding.cardHistory.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frameContainer, HistoryFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun filterList(query: String, category: String) {
        val filteredList = SeminarData.list.filter { seminar ->
            val matchesCategory =
                category == "Semua" || seminar.kategori.equals(category, ignoreCase = true)
            val matchesQuery =
                seminar.nama.contains(query, ignoreCase = true)

            matchesCategory && matchesQuery
        }
        adapter.updateList(filteredList)
    }

    private fun saveProfileImage(uriString: String) {
        val sharedPref = requireActivity().getSharedPreferences("USER_PREF", Context.MODE_PRIVATE)
        sharedPref.edit {
            putString("profile_image", uriString)
        }
    }

    private fun setupRecyclerView() {
        adapter = SeminarAdapter(emptyList()) { seminar ->
            openForm(seminar)
        }

        binding.rvSeminar.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSeminar.adapter = adapter

        filterList(currentQuery, currentCategory)
    }

    private fun openForm(seminar: Seminar) {
        val fragment = FormSeminarFragment().apply {
            arguments = Bundle().apply {
                putString("key_seminar", seminar.nama)
                putString("key_tanggal", seminar.tanggal)
                putString("key_jam", seminar.jam)
                putString("key_lokasi", seminar.lokasi)
                putString("key_kategori", seminar.kategori)
            }
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.frameContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
