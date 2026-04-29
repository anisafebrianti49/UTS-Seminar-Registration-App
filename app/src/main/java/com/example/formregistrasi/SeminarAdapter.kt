package com.example.formregistrasi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SeminarAdapter(
    // 1. Ubah 'val' menjadi 'var' agar list bisa diupdate
    private var listSeminar: List<Seminar>,
    private val onItemClick: (Seminar) -> Unit
) : RecyclerView.Adapter<SeminarAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val btnDaftar: Button = view.findViewById(R.id.btnDaftar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_seminar, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val seminar = listSeminar[position]
        holder.tvTitle.text = seminar.nama
        holder.tvDate.text = seminar.tanggal
        holder.btnDaftar.setOnClickListener { onItemClick(seminar) }
    }

    override fun getItemCount() = listSeminar.size

    // 2. Tambahkan fungsi ini agar HomeFragment bisa mengirim list baru hasil filter
    fun updateList(newList: List<Seminar>) {
        listSeminar = newList
        notifyDataSetChanged() // Perintah sakti untuk menggambar ulang RecyclerView
    }
}