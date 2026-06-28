package administrasilogistik;

import java.util.Scanner;

public class Buyer extends User {
    private GudangManager manager;
    private Scanner scanner;

    public Buyer(String namaBuyer, GudangManager manager) {
        super(namaBuyer, "Buyer");
        this.manager = manager;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void tampilProfil() {
        super.tampilProfil();
        System.out.println("  Hak Akses : Buat PO, Buat Good Receipt, Bayar Invoice");
        System.out.println("========================================================");
    }

    public void buatPO() {
        System.out.println("\n========================================================");
        System.out.println("              BUAT PURCHASE ORDER");
        System.out.println("========================================================");
        try {
            // IO - input data PO
            System.out.print("Nomor PO         : ");
            String nomorPO = scanner.nextLine();
            if (nomorPO.isEmpty()) throw new Exception("Nomor PO tidak boleh kosong!");

            // SELEKSI - cek PO sudah ada
            if (manager.cariPO(nomorPO) != null) {
                throw new Exception("Nomor PO " + nomorPO + " sudah digunakan!");
            }

            System.out.print("Tanggal (dd/mm/yyyy): ");
            String tanggal = scanner.nextLine();
            if (tanggal.isEmpty()) throw new Exception("Tanggal tidak boleh kosong!");

            System.out.print("Nama Pembuat     : ");
            String namaPembuat = scanner.nextLine();
            if (namaPembuat.isEmpty()) throw new Exception("Nama pembuat tidak boleh kosong!");

            System.out.print("Keterangan       : ");
            String keterangan = scanner.nextLine();

            // Buat objek PO (riwayat "Order Created" otomatis tercatat di constructor)
            PurchaseOrder po = new PurchaseOrder(nomorPO, tanggal, namaPembuat, keterangan);

            // Input item barang
            boolean tambahLagi = true;
            while (tambahLagi) {
                System.out.println("\n--- Tambah Item Barang ---");
                try {
                    System.out.print("Nama Barang      : ");
                    String namaBarang = scanner.nextLine();
                    if (namaBarang.isEmpty()) throw new Exception("Nama barang tidak boleh kosong!");

                    System.out.print("Quantity         : ");
                    int qty = Integer.parseInt(scanner.nextLine());
                    if (qty <= 0) throw new Exception("Quantity harus lebih dari 0!");

                    System.out.print("Harga Satuan     : ");
                    double harga = Double.parseDouble(scanner.nextLine());
                    if (harga <= 0) throw new Exception("Harga harus lebih dari 0!");

                    System.out.print("Jenis (EA/KG/UN/PCS/dll): ");
                    String jenis = scanner.nextLine().toUpperCase();
                    if (jenis.isEmpty()) throw new Exception("Jenis tidak boleh kosong!");

                    // Buat item dan tambahkan ke PO
                    POItem item = new POItem(namaBarang, qty, harga, jenis);
                    po.tambahItem(item);
                    System.out.println("Item berhasil ditambahkan!");
                    System.out.printf("   Total harga item: Rp%.0f%n", item.getTotalHarga());

                } catch (NumberFormatException e) {
                    System.out.println("Error: Input harus berupa angka!");
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }

                // SELEKSI - tanya tambah item lagi
                System.out.print("\nTambah item lagi? (y/n): ");
                String jawab = scanner.nextLine();
                tambahLagi = jawab.equalsIgnoreCase("y");
            }

            // SELEKSI - pastikan minimal ada 1 item
            if (po.getJumlahItem() == 0) {
                throw new Exception("PO harus memiliki minimal 1 item!");
            }

            // Simpan PO ke manager
            boolean simpanBerhasil = manager.simpanPO(po);
            if (!simpanBerhasil) {
                throw new Exception("Gagal menyimpan PO! Kapasitas penuh atau nomor PO duplikat.");
            }
            System.out.println("\nPO berhasil dibuat!");
            po.tampilInfo();

        } catch (NumberFormatException e) {
            System.out.println("Error: Input harus berupa angka!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buatGoodReceipt() {
        System.out.println("\n========================================================");
        System.out.println("              BUAT GOOD RECEIPT");
        System.out.println("========================================================");
        try {
            System.out.print("Masukkan Nomor PO: ");
            String nomorPO = scanner.nextLine();
            if (nomorPO.isEmpty()) throw new Exception("Nomor PO tidak boleh kosong!");

            // SELEKSI - cek PO ada
            PurchaseOrder po = manager.cariPO(nomorPO);
            if (po == null) throw new Exception("PO " + nomorPO + " tidak ditemukan!");

            // Ambil distribusi yang BELUM di-GR untuk PO ini
            Distribusi[] belumGR = manager.cariDistribusiBelumGRByPO(nomorPO);

            // PERULANGAN - hitung jumlah yang belum GR
            int jumlahBelumGR = 0;
            for (int i = 0; i < belumGR.length; i++) {
                if (belumGR[i] != null) jumlahBelumGR++;
            }

            // SELEKSI - cek ada distribusi yang bisa di-GR
            if (jumlahBelumGR == 0) {
                throw new Exception("Tidak ada pengiriman yang menunggu konfirmasi untuk PO ini!");
            }

            // Tampil daftar distribusi yang bisa dipilih
            System.out.println("\n--- Pengiriman Menunggu Konfirmasi ---");
            for (int i = 0; i < jumlahBelumGR; i++) {
                System.out.printf("%d. ", (i + 1));
                belumGR[i].tampilInfo();
            }

            System.out.print("\nPilih nomor pengiriman yang ingin dikonfirmasi: ");
            int pilihan = Integer.parseInt(scanner.nextLine());

            // SELEKSI - validasi pilihan
            if (pilihan < 1 || pilihan > jumlahBelumGR) {
                throw new Exception("Pilihan tidak valid!");
            }

            Distribusi dist = belumGR[pilihan - 1];

            // IO - input data GR
            System.out.print("\nNomor GR         : ");
            String nomorGR = scanner.nextLine();
            if (nomorGR.isEmpty()) throw new Exception("Nomor GR tidak boleh kosong!");

            // SELEKSI - cek nomor GR duplikat
            for (int i = 0; i < manager.getJumlahGR(); i++) {
                if (manager.getDaftarGR()[i].getNomorGR().equalsIgnoreCase(nomorGR)) {
                    throw new Exception("Nomor GR " + nomorGR + " sudah digunakan!");
                }
            }

            System.out.print("Tanggal Terima (dd/mm/yyyy): ");
            String tanggalTerima = scanner.nextLine();
            if (tanggalTerima.isEmpty()) throw new Exception("Tanggal terima tidak boleh kosong!");

            System.out.print("Nama Penerima    : ");
            String namaPenerima = scanner.nextLine();
            if (namaPenerima.isEmpty()) throw new Exception("Nama penerima tidak boleh kosong!");

            System.out.print("Catatan          : ");
            String catatan = scanner.nextLine();

            // Buat GR
            GoodReceipt gr = new GoodReceipt(
                    nomorGR, nomorPO,
                    dist.getNomorDistribusi(),
                    tanggalTerima, namaPenerima, catatan
            );

            // Simpan GR
            boolean simpanBerhasil = manager.simpanGR(gr);
            if (!simpanBerhasil) {
                throw new Exception("Gagal menyimpan GR! Kapasitas penuh atau nomor duplikat.");
            }

            // Update status
            dist.setStatusDistribusi("GR DONE");

            // Hitung kondisi terkini & update status PO
            boolean adaItemDikirim = true;
            boolean semuaItemDikirim = po.semuaItemSudahDikirim();
            boolean adaDistribusiDiGR = manager.adaDistribusiSudahGR(nomorPO);
            boolean semuaDistribusiDiGR = manager.semuaDistribusiSudahGR(nomorPO);

            po.updateStatus(adaItemDikirim, semuaItemDikirim, adaDistribusiDiGR, semuaDistribusiDiGR);

            if (po.getStatusPO().equals("RECEIVED")) {
                po.tambahRiwayat("Received", tanggalTerima, nomorGR);
            } else {
                po.tambahRiwayat("Partially Received", tanggalTerima, nomorGR);
            }

            System.out.println("\nGood Receipt berhasil dibuat!");
            gr.tampilInfo();
            System.out.println("\nStatus PO sekarang: " + po.getStatusPO());

            // SELEKSI - info status keseluruhan
            if (po.getStatusPO().equals("RECEIVED")) {
                System.out.println("Seluruh barang PO ini sudah diterima! PO siap untuk di-invoice.");
            } else {
                System.out.println("Masih ada barang yang belum dikirim/diterima sepenuhnya.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Error: Input harus berupa angka!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void lihatSemuaPO() {
        manager.tampilSemuaPO();
    }

    public void lihatSemuaGR() {
        manager.tampilSemuaGR();
    }

    public void cekDetailPO() {
        System.out.print("\nMasukkan Nomor PO: ");
        String nomorPO = scanner.nextLine();
        try {
            PurchaseOrder po = manager.cariPO(nomorPO);
            if (po == null) throw new Exception("PO " + nomorPO + " tidak ditemukan!");
            po.tampilInfo();

            // Tampil semua distribusi terkait PO ini
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

            // Tampil semua GR terkait PO ini
            GoodReceipt[] semuaGR = manager.cariSemuaGRByPO(nomorPO);
            System.out.println("\n--- Riwayat Good Receipt ---");
            boolean adaGR = false;
            // PERULANGAN - tampil semua GR
            for (int i = 0; i < semuaGR.length; i++) {
                if (semuaGR[i] != null) {
                    semuaGR[i].tampilInfo();
                    adaGR = true;
                }
            }
            if (!adaGR) {
                System.out.println("(Belum ada penerimaan barang)");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void cekInvoice() {
        System.out.println("\n========================================================");
        System.out.println("              DAFTAR INVOICE");
        System.out.println("========================================================");

        manager.tampilSemuaInvoice();

        // Tampil total semua tagihan UNPAID
        double totalUnpaid = manager.hitungTotalUnpaid();
        if (totalUnpaid > 0) {
            System.out.println("========================================================");
            System.out.printf("  Total Tagihan Belum Dibayar : Rp%.0f%n", totalUnpaid);
            System.out.println("========================================================");
        }
    }

    public void bayarInvoice() {
        System.out.println("\n========================================================");
        System.out.println("              PEMBAYARAN INVOICE");
        System.out.println("========================================================");
        try {
            // Ambil semua invoice UNPAID
            Invoice[] unpaidList = manager.getInvoiceUnpaid();

            // PERULANGAN - hitung jumlah yang UNPAID
            int jumlahUnpaid = 0;
            for (int i = 0; i < unpaidList.length; i++) {
                if (unpaidList[i] != null) jumlahUnpaid++;
            }

            // SELEKSI - cek ada invoice UNPAID
            if (jumlahUnpaid == 0) {
                System.out.println("Tidak ada invoice yang belum dibayar!");
                return;
            }

            // Tampil semua invoice UNPAID
            System.out.println("\n--- Invoice Belum Dibayar ---");
            System.out.printf("  %-3s %-15s %-15s %-15s%n",
                    "No.", "No. Invoice", "No. PO", "Total Tagihan");
            System.out.println("  -----------------------------------------------");
            // PERULANGAN - tampil list unpaid
            for (int i = 0; i < jumlahUnpaid; i++) {
                System.out.printf("  %-3d %-15s %-15s Rp%.0f%n",
                        (i + 1),
                        unpaidList[i].getNomorInvoice(),
                        unpaidList[i].getNomorPO(),
                        unpaidList[i].getTotalTagihan());
            }

            // Hitung & tampil grand total unpaid
            double totalUnpaid = manager.hitungTotalUnpaid();
            System.out.println("  -----------------------------------------------");
            System.out.printf("  Total Keseluruhan: Rp%.0f%n", totalUnpaid);

            // SELEKSI - pilih bayar semua atau pilih
            System.out.println("\nOpsi Pembayaran:");
            System.out.println("1. Bayar Semua Invoice");
            System.out.println("2. Pilih Invoice Tertentu");
            System.out.println("0. Batal");
            System.out.print("Pilih: ");
            int pilihanBayar = Integer.parseInt(scanner.nextLine());

            if (pilihanBayar == 0) {
                System.out.println("Pembayaran dibatalkan.");
                return;
            }

            // IO - input data pembayar
            System.out.print("\nNama Pembayar    : ");
            String namaPembayar = scanner.nextLine();
            if (namaPembayar.isEmpty()) throw new Exception("Nama pembayar tidak boleh kosong!");

            System.out.print("Tanggal Bayar (dd/mm/yyyy): ");
            String tanggalBayar = scanner.nextLine();
            if (tanggalBayar.isEmpty()) throw new Exception("Tanggal bayar tidak boleh kosong!");

            if (pilihanBayar == 1) {
                // ===== BAYAR SEMUA =====
                double totalBayar = totalUnpaid;

                // Konfirmasi pembayaran
                System.out.println("\nKonfirmasi Pembayaran Semua Invoice:");
                System.out.printf("   Total Bayar: Rp%.0f%n", totalBayar);
                System.out.print("Konfirmasi? (y/n): ");
                String konfirmasi = scanner.nextLine();
                if (!konfirmasi.equalsIgnoreCase("y")) {
                    System.out.println("Pembayaran dibatalkan.");
                    return;
                }

                // PERULANGAN - update semua invoice jadi PAID
                int terbayar = 0;
                for (int i = 0; i < jumlahUnpaid; i++) {
                    unpaidList[i].setStatusInvoice("PAID");
                    unpaidList[i].setTanggalBayar(tanggalBayar);
                    unpaidList[i].setNamaPembayar(namaPembayar);
                    terbayar++;
                }

                System.out.println("\n" + terbayar + " Invoice berhasil dibayar!");
                System.out.printf("   Total Dibayar: Rp%.0f%n", totalBayar);

            } else if (pilihanBayar == 2) {
                // ===== BAYAR PILIH =====
                boolean selesai = false;
                double totalDibayar = 0;
                int jumlahDibayar = 0;

                while (!selesai) {
                    System.out.print("\nMasukkan Nomor Invoice (ketik 'selesai' untuk stop): ");
                    String inputInvoice = scanner.nextLine();

                    // SELEKSI - cek keyword selesai
                    if (inputInvoice.equalsIgnoreCase("selesai")) {
                        selesai = true;
                        continue;
                    }

                    // SELEKSI - cek invoice ada
                    Invoice inv = manager.cariInvoice(inputInvoice);
                    if (inv == null) {
                        System.out.println("Invoice " + inputInvoice + " tidak ditemukan!");
                        continue;
                    }

                    // SELEKSI - cek invoice sudah PAID
                    if (inv.getStatusInvoice().equals("PAID")) {
                        System.out.println("Invoice " + inputInvoice + " sudah dibayar!");
                        continue;
                    }

                    // Konfirmasi per invoice
                    System.out.printf("   Invoice: %s | Total: Rp%.0f%n",
                            inv.getNomorInvoice(), inv.getTotalTagihan());
                    System.out.print("   Bayar invoice ini? (y/n): ");
                    String konfirmasiItem = scanner.nextLine();

                    if (konfirmasiItem.equalsIgnoreCase("y")) {
                        inv.setStatusInvoice("PAID");
                        inv.setTanggalBayar(tanggalBayar);
                        inv.setNamaPembayar(namaPembayar);
                        totalDibayar += inv.getTotalTagihan();
                        jumlahDibayar++;
                        System.out.println("Invoice " + inv.getNomorInvoice() + " berhasil dibayar!");
                    }
                }

                // SELEKSI - cek ada yang dibayar
                if (jumlahDibayar == 0) {
                    System.out.println("Tidak ada invoice yang dibayar.");
                    return;
                }

                System.out.println("\n" + jumlahDibayar + " Invoice berhasil dibayar!");
                System.out.printf("   Total Dibayar: Rp%.0f%n", totalDibayar);

            } else {
                throw new Exception("Pilihan tidak valid!");
            }

        } catch (NumberFormatException e) {
            System.out.println("Error: Input harus berupa angka!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}