package administrasilogistik;

import java.util.Scanner;

public class Supplier extends User {
    private GudangManager manager;
    private Scanner scanner;

    public Supplier(String namaSupplier, GudangManager manager) {
        super(namaSupplier, "Supplier");
        this.manager = manager;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void tampilProfil() {
        super.tampilProfil();
        System.out.println("  Hak Akses : Kirim Barang, Buat Invoice");
        System.out.println("========================================================");
    }

    // ===== PROSES PO (KIRIM BARANG) =====
    public void prosesPO() {
        System.out.println("\n========================================================");
        System.out.println("              PROSES PURCHASE ORDER");
        System.out.println("========================================================");
        try {
            // IO - input nomor PO
            System.out.print("Masukkan Nomor PO: ");
            String nomorPO = scanner.nextLine();
            if (nomorPO.isEmpty()) throw new Exception("Nomor PO tidak boleh kosong!");

            // SELEKSI - cek PO ada
            PurchaseOrder po = manager.cariPO(nomorPO);
            if (po == null) throw new Exception("PO " + nomorPO + " tidak ditemukan!");

            // SELEKSI - cek masih ada item yang bisa dikirim
            if (!po.masihAdaItemBelumDikirim()) {
                throw new Exception("Semua item di PO ini sudah dikirim! Status: " + po.getStatusPO());
            }

            // Tampil detail PO
            System.out.println("\n--- Detail PO ---");
            po.tampilInfo();

            // Tampil hanya item yang BELUM DIKIRIM untuk dipilih
            System.out.println("\n--- Item yang Belum Dikirim ---");
            POItem[] semuaItem = po.getDaftarItem();
            int[] indexBelumDikirim = new int[po.getJumlahItem()];
            int jumlahBelumDikirim = 0;

            // PERULANGAN - kumpulkan index item yang belum dikirim
            for (int i = 0; i < po.getJumlahItem(); i++) {
                if (semuaItem[i].getStatusItem().equals("BELUM DIKIRIM")) {
                    System.out.printf("  %d. ", (jumlahBelumDikirim + 1));
                    semuaItem[i].tampilInfo();
                    indexBelumDikirim[jumlahBelumDikirim] = i;
                    jumlahBelumDikirim++;
                }
            }

            // IO - input data distribusi
            System.out.print("\nNomor Distribusi : ");
            String nomorDist = scanner.nextLine();
            if (nomorDist.isEmpty()) throw new Exception("Nomor distribusi tidak boleh kosong!");

            // SELEKSI - cek nomor distribusi duplikat
            if (manager.cariDistribusi(nomorDist) != null) {
                throw new Exception("Nomor Distribusi " + nomorDist + " sudah digunakan!");
            }

            System.out.print("Tanggal Pengiriman (dd/mm/yyyy): ");
            String tanggalKirim = scanner.nextLine();
            if (tanggalKirim.isEmpty()) throw new Exception("Tanggal pengiriman tidak boleh kosong!");

            // SELEKSI - pilih jenis kiriman
            System.out.println("\nJenis Pengiriman:");
            System.out.println("1. FULL (kirim semua item yang masih tersisa)");
            System.out.println("2. PARTIAL (pilih item tertentu saja)");
            System.out.print("Pilih (1/2)      : ");
            int pilihanJenis = Integer.parseInt(scanner.nextLine());

            // Buat objek distribusi
            Distribusi dist = new Distribusi(nomorDist, nomorPO, tanggalKirim, getNama());

            if (pilihanJenis == 1) {
                // ===== FULL - kirim semua item yang belum dikirim =====
                for (int i = 0; i < jumlahBelumDikirim; i++) {
                    POItem item = semuaItem[indexBelumDikirim[i]];
                    item.setStatusItem("SUDAH DIKIRIM");
                    dist.tambahItem(new DistribusiItem(
                            item.getNamaBarang(), item.getQuantity(), item.getJenis()
                    ));
                }
                System.out.println("Semua item tersisa dikirim sekaligus (FULL).");

            } else if (pilihanJenis == 2) {
                // ===== PARTIAL - pilih item satu per satu =====
                boolean adaYangDipilih = false;
                boolean selesaiPilih = false;

                while (!selesaiPilih) {
                    System.out.print("\nPilih nomor item yang dikirim (0 untuk selesai): ");
                    int pilihanItem = Integer.parseInt(scanner.nextLine());

                    // SELEKSI - cek selesai
                    if (pilihanItem == 0) {
                        selesaiPilih = true;
                        continue;
                    }

                    // SELEKSI - validasi nomor pilihan
                    if (pilihanItem < 1 || pilihanItem > jumlahBelumDikirim) {
                        System.out.println("Nomor item tidak valid!");
                        continue;
                    }

                    POItem item = semuaItem[indexBelumDikirim[pilihanItem - 1]];

                    // SELEKSI - cek item sudah dipilih sebelumnya
                    if (item.getStatusItem().equals("SUDAH DIKIRIM")) {
                        System.out.println("Item ini sudah dipilih untuk dikirim!");
                        continue;
                    }

                    item.setStatusItem("SUDAH DIKIRIM");
                    dist.tambahItem(new DistribusiItem(
                            item.getNamaBarang(), item.getQuantity(), item.getJenis()
                    ));
                    adaYangDipilih = true;
                    System.out.println("'" + item.getNamaBarang() + "' ditambahkan ke pengiriman ini.");
                }

                // SELEKSI - pastikan minimal 1 item dipilih
                if (!adaYangDipilih) {
                    throw new Exception("Tidak ada item yang dipilih untuk dikirim!");
                }

            } else {
                throw new Exception("Pilihan jenis pengiriman tidak valid!");
            }

            // Simpan distribusi
            boolean simpanBerhasil = manager.simpanDistribusi(dist);
            if (!simpanBerhasil) {
                throw new Exception("Gagal menyimpan distribusi! Kapasitas penuh atau nomor duplikat.");
            }

            // Hitung kondisi terkini & update status PO
            boolean adaItemDikirim = !po.masihAdaItemBelumDikirim() || true; // minimal item barusan sudah dikirim
            boolean semuaItemDikirim = po.semuaItemSudahDikirim();
            boolean adaDistribusiDiGR = manager.adaDistribusiSudahGR(nomorPO);
            boolean semuaDistribusiDiGR = manager.semuaDistribusiSudahGR(nomorPO);

            po.updateStatus(adaItemDikirim, semuaItemDikirim, adaDistribusiDiGR, semuaDistribusiDiGR);

            // Catat riwayat - event "Shipped" hanya dicatat kalau SEMUA item sudah terkirim di titik ini
            if (semuaItemDikirim) {
                po.tambahRiwayat("Shipped", tanggalKirim, nomorDist);
            } else {
                po.tambahRiwayat("Partially Shipped", tanggalKirim, nomorDist);
            }

            System.out.println("\nPengiriman berhasil diproses!");
            dist.tampilInfo();
            System.out.println("\nStatus PO sekarang: " + po.getStatusPO());

            // SELEKSI - info tambahan kalau masih ada sisa
            if (po.masihAdaItemBelumDikirim()) {
                System.out.println("Masih ada item yang belum dikirim, PO ini bisa diproses lagi nanti.");
            } else {
                System.out.println("Semua item PO ini sudah dikirim!");
            }

        } catch (NumberFormatException e) {
            System.out.println("Error: Input harus berupa angka!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ===== BUAT INVOICE =====
    public void buatInvoice() {
        System.out.println("\n========================================================");
        System.out.println("              BUAT INVOICE");
        System.out.println("========================================================");
        try {
            // IO - input nomor PO
            System.out.print("Masukkan Nomor PO: ");
            String nomorPO = scanner.nextLine();
            if (nomorPO.isEmpty()) throw new Exception("Nomor PO tidak boleh kosong!");

            // SELEKSI - cek PO ada
            PurchaseOrder po = manager.cariPO(nomorPO);
            if (po == null) throw new Exception("PO " + nomorPO + " tidak ditemukan!");

            // SELEKSI - syarat utama, status PO harus RECEIVED
            if (!po.getStatusPO().equals("RECEIVED")) {
                throw new Exception("PO belum sepenuhnya diterima Buyer! Status saat ini: " + po.getStatusPO());
            }

            // SELEKSI - cek invoice untuk PO ini sudah ada
            if (manager.cariInvoiceByPO(nomorPO) != null) {
                throw new Exception("Invoice untuk PO " + nomorPO + " sudah pernah dibuat!");
            }

            // Ambil semua GR terkait PO ini (buat referensi & tampilan)
            GoodReceipt[] semuaGR = manager.cariSemuaGRByPO(nomorPO);

            // Tampil detail PO & semua GR
            System.out.println("\n--- Detail PO ---");
            po.tampilInfo();
            System.out.println("\n--- Semua Good Receipt Terkait ---");
            for (int i = 0; i < semuaGR.length; i++) {
                if (semuaGR[i] != null) {
                    semuaGR[i].tampilInfo();
                }
            }

            // Konfirmasi sebelum buat invoice
            System.out.println("\nKonfirmasi pembuatan invoice:");
            System.out.printf("   Total Tagihan: Rp%.0f%n", po.getGrandTotal());
            System.out.print("Konfirmasi? (y/n): ");
            String konfirmasi = scanner.nextLine();
            if (!konfirmasi.equalsIgnoreCase("y")) {
                System.out.println("Pembuatan invoice dibatalkan.");
                return;
            }

            // IO - input data invoice
            System.out.print("\nNomor Invoice    : ");
            String nomorInvoice = scanner.nextLine();
            if (nomorInvoice.isEmpty()) throw new Exception("Nomor invoice tidak boleh kosong!");

            // SELEKSI - cek nomor invoice duplikat
            if (manager.cariInvoice(nomorInvoice) != null) {
                throw new Exception("Nomor Invoice " + nomorInvoice + " sudah digunakan!");
            }

            System.out.print("Tanggal Invoice (dd/mm/yyyy): ");
            String tanggalInvoice = scanner.nextLine();
            if (tanggalInvoice.isEmpty()) throw new Exception("Tanggal invoice tidak boleh kosong!");

            // Ambil nomor GR pertama sebagai referensi utama (boleh disesuaikan)
            String nomorGRReferensi = semuaGR.length > 0 && semuaGR[0] != null
                    ? semuaGR[0].getNomorGR() : "-";

            // Buat invoice
            Invoice invoice = new Invoice(
                    nomorInvoice, nomorPO,
                    nomorGRReferensi, getNama(),
                    tanggalInvoice, po.getGrandTotal()
            );

            // Simpan invoice
            boolean simpanBerhasil = manager.simpanInvoice(invoice);
            if (!simpanBerhasil) {
                throw new Exception("Gagal menyimpan invoice! Kapasitas penuh atau nomor duplikat.");
            }

            System.out.println("\nInvoice berhasil dibuat & dikirim ke Buyer!");
            invoice.tampilInfo();

        } catch (NumberFormatException e) {
            System.out.println("Error: Input harus berupa angka!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ===== LIHAT SEMUA PO =====
    public void lihatSemuaPO() {
        manager.tampilSemuaPO();
    }
    
    // ===== CEK DETAIL PO (untuk lihat barang mana yang belum dikirim) =====
    public void cekDetailPO() {
        System.out.print("\nMasukkan Nomor PO: ");
        String nomorPO = scanner.nextLine();
        try {
            // SELEKSI - cek PO ada
            PurchaseOrder po = manager.cariPO(nomorPO);
            if (po == null) throw new Exception("PO " + nomorPO + " tidak ditemukan!");

            po.tampilInfo();

            // Tampil ringkasan khusus item yang belum dikirim
            System.out.println("\n--- Ringkasan Item Belum Dikirim ---");
            POItem[] semuaItem = po.getDaftarItem();
            boolean adaBelumDikirim = false;

            // PERULANGAN - cek tiap item
            for (int i = 0; i < po.getJumlahItem(); i++) {
                if (semuaItem[i].getStatusItem().equals("BELUM DIKIRIM")) {
                    System.out.printf("  %d. ", (i + 1));
                    semuaItem[i].tampilInfo();
                    adaBelumDikirim = true;
                }
            }

            if (!adaBelumDikirim) {
                System.out.println("  (Semua item sudah dikirim)");
            }

            // Tampil riwayat distribusi terkait PO ini
            Distribusi[] semuaDist = manager.cariSemuaDistribusiByPO(nomorPO);
            System.out.println("\n--- Riwayat Distribusi ---");
            boolean adaDistribusi = false;
            // PERULANGAN - tampil semua distribusi
            for (int i = 0; i < semuaDist.length; i++) {
                if (semuaDist[i] != null) {
                    semuaDist[i].tampilInfo();
                    adaDistribusi = true;
                }
            }
            if (!adaDistribusi) {
                System.out.println("(Belum ada pengiriman)");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ===== LIHAT SEMUA INVOICE =====
    public void lihatSemuaInvoice() {
        manager.tampilSemuaInvoice();
    }
}