package com.example.formregistrasi

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

class ResultFragment : Fragment(R.layout.fragment_result) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvNama = view.findViewById<TextView>(R.id.tvNama)
        val tvEmail = view.findViewById<TextView>(R.id.tvEmail)
        val tvHP = view.findViewById<TextView>(R.id.tvHP)
        val tvInstansi = view.findViewById<TextView>(R.id.tvInstansi)
        val tvStatus = view.findViewById<TextView>(R.id.tvStatus)
        val tvSeminar = view.findViewById<TextView>(R.id.tvSeminar)
        val tvId = view.findViewById<TextView>(R.id.tvId)
        val tvTanggalJam = view.findViewById<TextView>(R.id.tvTanggalJam)
        val tvLokasi = view.findViewById<TextView>(R.id.tvLokasi)

        val imgQR = view.findViewById<ImageView>(R.id.imgQR)

        val btnKembali = view.findViewById<Button>(R.id.btnKembali)
        val btnDownload = view.findViewById<Button>(R.id.btnDownload)
        val btnShare = view.findViewById<Button>(R.id.btnShare)

        val ticketView = view.findViewById<View>(R.id.rootTicket)

        val sharedPref = requireActivity()
            .getSharedPreferences("TICKET_PREF", Context.MODE_PRIVATE)

        // =========================
        // ✅ AMBIL DATA DARI ARGUMENTS
        // =========================
        val nama = arguments?.getString("key_nama") ?: "-"
        val email = arguments?.getString("key_email") ?: "-"
        val hp = arguments?.getString("key_hp") ?: "-"
        val instansi = arguments?.getString("key_instansi") ?: "-"
        val status = arguments?.getString("key_status") ?: "-"
        val seminar = arguments?.getString("key_seminar") ?: "-"
        val tanggal = arguments?.getString("key_tanggal") ?: "-"
        val jam = arguments?.getString("key_jam") ?: "-"
        val lokasi = arguments?.getString("key_lokasi") ?: "-"

        // =========================
        // ID
        // =========================
        val idPeserta = "SEM" + System.currentTimeMillis().toString().takeLast(5)
        tvId.text = "ID: $idPeserta"

        // =========================
        // SET TEXT
        // =========================
        tvNama.text = nama
        tvEmail.text = email
        tvHP.text = hp
        tvInstansi.text = instansi
        tvStatus.text = status
        tvSeminar.text = seminar
        tvTanggalJam.text = "$tanggal • $jam"
        tvLokasi.text = lokasi

        // =========================
        // QR CODE
        // =========================
        val qrData = "$idPeserta|$nama|$seminar"

        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(qrData, BarcodeFormat.QR_CODE, 400, 400)
            imgQR.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Gagal generate QR", Toast.LENGTH_SHORT).show()
        }

        // =========================
        // SIMPAN HISTORY
        // =========================
        saveToHistory(sharedPref, idPeserta, nama, seminar, tanggal, jam, lokasi)

        // =========================
        // DOWNLOAD PDF
        // =========================
        btnDownload.setOnClickListener {
            ticketView.post {
                val bitmap = getBitmapFromView(ticketView)
                saveAsPDF(bitmap, idPeserta)
            }
        }

        // =========================
        // SHARE WA
        // =========================
        btnShare.setOnClickListener {
            ticketView.post {
                val bitmap = getBitmapFromView(ticketView)
                shareToWhatsApp(bitmap, idPeserta)
            }
        }

        // =========================
        //  KEMBALI (FIX)
        // =========================
        btnKembali.setOnClickListener {
            parentFragmentManager.popBackStack(
                null,
                androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
            )

            parentFragmentManager.beginTransaction()
                .replace(R.id.frameContainer, HomeFragment())
                .commit()
        }
    }

    // =========================
    // HISTORY
    // =========================
    private fun saveToHistory(
        sharedPref: android.content.SharedPreferences,
        id: String,
        nama: String,
        seminar: String,
        tanggal: String,
        jam: String,
        lokasi: String
    ) {
        val data = sharedPref.getString("tickets", "[]")
        val jsonArray = JSONArray(data)

        val obj = JSONObject()
        obj.put("id", id)
        obj.put("nama", nama)
        obj.put("seminar", seminar)
        obj.put("tanggal", tanggal)
        obj.put("jam", jam)
        obj.put("lokasi", lokasi)

        jsonArray.put(obj)

        sharedPref.edit().putString("tickets", jsonArray.toString()).apply()
    }

    private fun shareToWhatsApp(bitmap: Bitmap, id: String) {
        try {
            val file = File(requireContext().cacheDir, "ticket_$id.png")
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.close()

            val uri: Uri = FileProvider.getUriForFile(
                requireContext(),
                requireContext().packageName + ".provider",
                file
            )

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setPackage("com.whatsapp")

            startActivity(intent)

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "WhatsApp tidak tersedia", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun saveAsPDF(bitmap: Bitmap, id: String) {
        val document = PdfDocument()

        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = document.startPage(pageInfo)

        page.canvas.drawBitmap(bitmap, 0f, 0f, null)
        document.finishPage(page)

        val file = File(requireContext().getExternalFilesDir(null), "ticket_$id.pdf")

        try {
            document.writeTo(FileOutputStream(file))
            Toast.makeText(requireContext(), "PDF tersimpan:\n${file.path}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Gagal simpan PDF", Toast.LENGTH_SHORT).show()
        }

        document.close()
    }
}