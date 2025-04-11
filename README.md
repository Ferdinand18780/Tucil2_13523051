# Tucil2_13523051

# Kompresi Gambar dengan Quadtree

Proyek ini merupakan implementasi algoritma kompresi gambar menggunakan struktur data **Quadtree**. Gambar akan dibagi menjadi beberapa blok sesuai tingkat kompleksitas warna berdasarkan metrik tertentu (seperti varians, MAD, pixel difference, atau entropi). Hasil akhirnya adalah gambar yang sudah dikompresi serta animasi GIF yang menunjukkan proses kompresinya.

---

## Fitur

- Kompresi gambar berbasis Quadtree.
- Mendukung beberapa metrik error:
  - Varians
  - Mean Absolute Deviation (MAD)
  - Selisih piksel maksimum
  - Entropi
- Visualisasi hasil kompresi dalam bentuk gambar.
- Bonus: Pembuatan GIF animasi dari proses kompresi.

---

## ⚙️ Cara Menjalankan

### 📦 Jika folder `bin/` masih kosong (belum dikompilasi):
1. Pastikan semua file `.java` Anda ada di folder `src/`, dan file `.java` dari library eksternal (misalnya `AnimatedGifEncoder`) ada di dalam folder `lib/`.
2. Jalankan perintah berikut untuk mengompilasi semua file ke folder `bin/`:

```bash
javac -d bin lib/com/madgag/gif/fmsware/*.java
javac -d bin -cp bin src/*.java
```

### 📦 Jika folder `bin/` sudah terisi:
Langsung jalankan program:

```bash
java -cp bin Main
```

---

## 🧩 Library Eksternal

Proyek ini menggunakan library eksternal untuk membuat file **GIF animasi** dari serangkaian gambar hasil kompresi.

Library yang digunakan:

- [`animated-gif-lib-for-java`](https://github.com/rtyley/animated-gif-lib-for-java)  
  Library ini merupakan repackaging dari `AnimatedGifEncoder` karya **Kevin Weiner (FM Software)**, dan tersedia di GitHub oleh **rtyley**. Library ini digunakan untuk mempermudah proses encoding frame menjadi satu file GIF.

---

## 🖼️ Output

- Hasil kompresi gambar disimpan dalam folder `/test`.
- GIF animasi progres kompresi juga tersimpan dalam folder tersebut.

---

## 👤 Author

13523051 — [Ferdinand Gabe Tua Sinaga]
