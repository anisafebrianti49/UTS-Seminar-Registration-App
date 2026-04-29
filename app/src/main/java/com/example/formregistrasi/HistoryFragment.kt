package com.example.formregistrasi

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray

class HistoryFragment : Fragment(R.layout.fragment_history) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerHistory)
        val layoutEmpty = view.findViewById<View>(R.id.layoutEmpty)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val list = mutableListOf<Ticket>()

        val sharedPref = requireActivity()
            .getSharedPreferences("TICKET_PREF", 0)

        val data = sharedPref.getString("tickets", "[]")

        try {
            val jsonArray = JSONArray(data)

            // 🔥 CEK KOSONG
            if (jsonArray.length() == 0) {
                layoutEmpty.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                return
            }

            // 🔥 AMBIL DATA
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)

                list.add(
                    Ticket(
                        obj.optString("id", "-"),
                        obj.optString("nama", "-"),
                        obj.optString("seminar", "-"),
                        obj.optString("tanggal", "-"),
                        obj.optString("jam", "-"),
                        obj.optString("lokasi", "-")
                    )
                )
            }

        } catch (e: Exception) {
            // 🔥 HANDLE ERROR (JSON rusak/null)
            layoutEmpty.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            return
        }

        // 🔥 SET ADAPTER
        val adapter = TicketAdapter(list) { ticket ->

            val bundle = Bundle().apply {
                putString("key_nama", ticket.nama)
                putString("key_seminar", ticket.seminar)
                putString("key_tanggal", ticket.tanggal)
                putString("key_jam", ticket.jam)
                putString("key_lokasi", ticket.lokasi)
                putString("key_id", ticket.id)
            }

            val fragment = ResultFragment()
            fragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.frameContainer, fragment)
                .addToBackStack(null)
                .commit()
        }

        recyclerView.adapter = adapter

        // 🔥 TAMPILKAN LIST
        layoutEmpty.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }
}