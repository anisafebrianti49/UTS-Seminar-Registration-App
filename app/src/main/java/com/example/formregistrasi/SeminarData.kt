package com.example.formregistrasi

data class Seminar(
    val nama: String,
    val tanggal: String,
    val jam: String,
    val lokasi: String,
    val kategori: String // Tambahkan baris ini
)

object SeminarData {

    val list = listOf(
        Seminar(
            "Data Science",
            "12 Mei 2026",
            "09:00 WIB",
            "Aula Kampus A",
            "Tech" // Tambahkan kategori di setiap item
        ),
        Seminar(
            "Web Development",
            "13 Mei 2026",
            "10:00 WIB",
            "Lantai 3 Gedung B",
            "Tech"
        ),
        Seminar(
            "Public Speaking",
            "14 Mei 2026",
            "09:00 WIB",
            "Aula Kampus A",
            "Soft Skills"
        ),
        Seminar(
            "UI/UX Design",
            "15 Mei 2026",
            "13:00 WIB",
            "Gedung B lantai 3",
            "Tech"
        ),
        Seminar(
            "Problem Solving & Critical Thinking",
            "17 Mei 2026",
            "13:00 WIB",
            "Aula Kampus A",
            "Soft Skills"
        ),
        Seminar(
            "Leadership",
            "17 Mei 2026",
            "09:00 WIB",
            "Gedung B lantai 3",
            "Soft Skills"
        )
    )
}