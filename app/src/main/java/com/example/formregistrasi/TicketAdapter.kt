package com.example.formregistrasi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TicketAdapter(
    private val list: List<Ticket>,
    private val onClick: (Ticket) -> Unit
) : RecyclerView.Adapter<TicketAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvSeminar: TextView = view.findViewById(R.id.tvItemSeminar)
        val tvTanggal: TextView = view.findViewById(R.id.tvItemTanggal)
        val tvId: TextView = view.findViewById(R.id.tvItemId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ticket, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.tvSeminar.text = item.seminar
        holder.tvTanggal.text = "${item.tanggal} • ${item.jam}"
        holder.tvId.text = "ID: ${item.id}"

        holder.itemView.setOnClickListener {
            onClick(item)
        }
    }
}