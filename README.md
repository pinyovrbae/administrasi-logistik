# Sistem Administrasi & Logistik (Purchase Order - Invoice)

Aplikasi konsol berbasis Java NetBeans yang mensimulasikan alur procurement antara **Buyer** dan **Supplier**, mulai dari pembuatan Purchase Order (PO), pengiriman barang (Distribusi), konfirmasi penerimaan (Good Receipt), hingga penagihan dan pembayaran (Invoice).

## Studi Kasus

Buyer membuat Purchase Order yang berisi daftar barang yang ingin dipesan. Supplier memproses PO tersebut dengan mengirim barang secara penuh (full) atau sebagian (parsial), dan Buyer mengonfirmasi penerimaan barang lewat Good Receipt. Setelah seluruh item dalam PO selesai dikirim dan diterima, Supplier dapat menerbitkan Invoice yang kemudian dibayar oleh Buyer.

## Cara Menjalankan

1. Buka project di Apache NetBeans.
2. Jalankan `Main.java`.
3. Pilih peran **Buyer** atau **Supplier** di menu utama, lalu ikuti instruksi pada masing-masing menu.

## Struktur Package

```
administrasilogistik/
├── POItem.java
├── PurchaseOrder.java
├── DistribusiItem.java
├── Distribusi.java
├── GoodReceipt.java
├── Invoice.java
├── GudangManager.java
├── Buyer.java
├── Supplier.java
└── Main.java
```

## Alur Sistem Singkat

```
Buyer buat PO  ->  Supplier kirim barang (full/parsial)  ->  Buyer buat Good Receipt
      ->  (ulangi jika masih ada sisa item)  ->  Status PO: RECEIVED
      ->  Supplier buat Invoice  ->  Buyer bayar Invoice
```

Status PO berjalan otomatis mengikuti kondisi pengiriman dan penerimaan:

`OPEN -> PARTIALLY SHIPPED -> SHIPPED -> PARTIALLY RECEIVED -> RECEIVED`

Invoice hanya dapat dibuat ketika status PO sudah **RECEIVED** (seluruh item telah dikirim Supplier dan dikonfirmasi diterima oleh Buyer).

---

## Komponen Penilaian PBO

| No | Komponen | Nilai Maksimal | Lokasi Implementasi |
|----|----------|----------------|----------------------|
| 1 | Class | 5 | Seluruh file `.java` (10 class: `POItem`, `PurchaseOrder`, `DistribusiItem`, `Distribusi`, `GoodReceipt`, `Invoice`, `GudangManager`, `Buyer`, `Supplier`, `Main`) |
| 2 | Object | 5 | Instansiasi di `Main.java` (`new Buyer(...)`, `new Supplier(...)`, `new GudangManager()`) dan di dalam method seperti `new PurchaseOrder(...)`, `new POItem(...)`, `new Distribusi(...)`, `new GoodReceipt(...)`, `new Invoice(...)` |
| 3 | Atribut | 5 | Seluruh field `private` pada tiap class, contoh: `nomorPO`, `statusPO`, `daftarItem`, `grandTotal` pada `PurchaseOrder.java` |
| 4 | Constructor | 5 | Tiap class memiliki constructor, contoh `PurchaseOrder(String nomorPO, String tanggalDibuat, String namaPembuat, String keterangan)` |
| 5 | Mutator | 5 | Method `set...()` pada seluruh class, contoh `setStatusItem()` pada `POItem.java`, `setStatusDistribusi()` pada `Distribusi.java` |
| 6 | Accessor | 5 | Method `get...()` pada seluruh class, contoh `getStatusPO()`, `getGrandTotal()`, `getDaftarItem()` |
| 7 | Encapsulation | 5 | Seluruh atribut bertipe `private`, hanya dapat diakses lewat getter/setter di semua class |
| 8 | Inheritance | 5 | Tidak diterapkan secara eksplisit pada versi project ini (lihat catatan di bawah) |
| 9 | Polymorphism | 10 | Tidak diterapkan secara eksplisit pada versi project ini (lihat catatan di bawah) |
| 10 | Seleksi | 5 | `if-else` validasi input dan status di hampir seluruh method, contoh pengecekan status PO sebelum membuat Invoice pada `Supplier.buatInvoice()` |
| 11 | Perulangan | 5 | `for` dan `while` pada pencarian data array, contoh `cariPO()` dan `tampilSemuaPO()` di `GudangManager.java`, serta `while(tambahLagi)` pada `Buyer.buatPO()` |
| 12 | IO Sederhana | 10 | `Scanner` untuk input data di `Buyer.java` dan `Supplier.java` (input nomor PO, qty, harga, tanggal, dsb) |
| 13 | Array | 15 | `POItem[]`, `DistribusiItem[]`, `PurchaseOrder[]`, `Distribusi[]`, `GoodReceipt[]`, `Invoice[]` sebagai struktur penyimpanan utama di `PurchaseOrder.java`, `Distribusi.java`, dan `GudangManager.java` |
| 14 | Error Handling | 15 | `try-catch` pada seluruh method input di `Buyer.java` dan `Supplier.java`, menangani `NumberFormatException` dan `Exception` custom untuk validasi |

**Total tertutup tanpa Inheritance/Polymorphism: 85/100**

### Catatan Mengenai Inheritance dan Polymorphism

Pada desain akhir, seluruh class (`PurchaseOrder`, `Distribusi`, `GoodReceipt`, `Invoice`, `Buyer`, `Supplier`, dll) berdiri sendiri tanpa relasi `extends`, karena domain procurement (PO - Distribusi - GR - Invoice) lebih natural dimodelkan sebagai entitas independen yang saling berelasi lewat nomor referensi, bukan lewat hierarki turunan.

Apabila instruksi tugas mewajibkan kedua komponen ini, dapat ditambahkan dengan pendekatan berikut tanpa mengubah alur bisnis yang sudah berjalan:

- Membuat parent class `User` yang menyimpan atribut umum (nama, peran), kemudian `Buyer extends User` dan `Supplier extends User`.
- Membuat parent class `Dokumen` yang menyimpan atribut umum (nomor dokumen, tanggal, status), kemudian `PurchaseOrder`, `Distribusi`, `GoodReceipt`, dan `Invoice` masing-masing `extends Dokumen` dan meng-override method `tampilInfo()` sesuai kebutuhan tampilan masing-masing dokumen (Polymorphism).

## Fitur Utama

- Buyer dapat membuat Purchase Order dengan banyak item barang (nama, qty, harga satuan, jenis satuan).
- Supplier dapat memproses PO secara penuh atau parsial, dan dapat memproses PO yang sama berkali-kali selama masih ada item yang belum dikirim.
- Buyer dapat membuat Good Receipt per distribusi yang diterima, mendukung beberapa kali penerimaan untuk satu PO.
- Status PO dihitung otomatis berdasarkan kondisi pengiriman dan penerimaan terkini, lengkap dengan riwayat/histori setiap perubahan status.
- Supplier hanya dapat membuat Invoice setelah seluruh item PO selesai dikirim dan diterima.
- Buyer dapat membayar satu, beberapa, atau seluruh Invoice yang belum lunas (unpaid) sekaligus.

## Keterbatasan

- Data tersimpan hanya pada memori program (array), sehingga akan hilang ketika program ditutup. Tidak ada penyimpanan ke database atau file eksternal.
- Tidak terdapat sistem autentikasi/login sungguhan untuk membedakan Buyer dan Supplier; pemilihan peran dilakukan murni lewat menu.
- Kapasitas data dibatasi oleh ukuran array tetap (maksimal 50-100 entri tergantung jenis data).
