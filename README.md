# Tugas UTS - Seminar Registration App

Aplikasi Android untuk pendaftaran seminar yang dibuat sebagai tugas Ujian Tengah Semester (UTS) Pemrograman Mobile.
Aplikasi ini memungkinkan pengguna untuk melakukan registrasi, login, memilih seminar, mengisi form pendaftaran, serta melihat riwayat pendaftaran.

## Identitas

Nama: Anisa Febrianti
Mata Kuliah: Pemrograman Mobile
Tanggal: Mei 2025

---

## Fitur Aplikasi

### 1. Login dan Register

* User dapat membuat akun baru (register)
* User dapat login menggunakan akun yang sudah dibuat
* Data disimpan menggunakan SharedPreferences

### 2. Halaman Utama (Home)

* Menampilkan nama dan email user
* Menampilkan daftar seminar
* Fitur filter kategori:

  * Semua
  * Tech
  * Soft Skills
* Fitur pencarian seminar
* Upload foto profil

### 3. Form Pendaftaran Seminar

* User dapat memilih seminar
* Mengisi data pendaftaran
* Validasi input (tidak boleh kosong)
* Menampilkan dialog konfirmasi sebelum submit

### 4. Halaman Hasil

* Menampilkan data hasil pendaftaran
* Informasi seminar yang dipilih

### 5. Riwayat Pendaftaran

* Menampilkan daftar seminar yang pernah didaftarkan

### 6. Notifikasi

* Menampilkan indikator notifikasi

---

## Validasi yang Digunakan

* Field tidak boleh kosong
* Validasi dilakukan secara real-time saat input
* Pesan error ditampilkan kepada user

---

## Teknologi yang Digunakan

* Kotlin
* Android Studio
* RecyclerView
* SharedPreferences
* Fragment
* Material Design

---

## Video Penjelasan

Link video penjelasan aplikasi (YouTube):
https://youtu.be/GgdFzRFOmac?feature=shared


Isi video:

* Penjelasan halaman login
* Penjelasan halaman utama
* Penjelasan form pendaftaran
* Penjelasan validasi
* Penjelasan dialog
* Penjelasan halaman hasil
* Penjelasan kode

---

## Struktur Project

* LoginActivity: Halaman login
* RegisterActivity: Halaman register
* HomeFragment: Halaman utama
* FormSeminarFragment: Form pendaftaran
* HistoryFragment: Riwayat pendaftaran
* SeminarAdapter: Adapter RecyclerView

---

## Cara Menjalankan Project

1. Clone repository:
   git clone [https://github.com/anisafebrianti49/UTS-Seminar-Registration-App)

2. Buka project di Android Studio

3. Klik Run untuk menjalankan aplikasi

---

## Catatan

* Project ini dibuat untuk keperluan pembelajaran
* Menggunakan local storage tanpa database online
* Fokus pada implementasi fitur dasar aplikasi Android dan interaksi user
