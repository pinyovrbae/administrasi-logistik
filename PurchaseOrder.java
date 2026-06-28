package administrasilogistik;

public class PurchaseOrder {
    // ATRIBUT
    private String nomorPO;
    private String tanggalDibuat;
    private String namaPembuat;
    private POItem[] daftarItem;      // ARRAY item barang
    private int jumlahItem;
    private String keterangan;
    private String statusPO;          // OPEN, PARTIALLY SHIPPED, SHIPPED, PARTIALLY RECEIVED, RECEIVED
    private double grandTotal;
    private String[] riwayat;         // ARRAY riwayat/histori event PO
    private int jumlahRiwayat;

    // CONSTRUCTOR
    public PurchaseOrder(String nomorPO, String tanggalDibuat,
                         String namaPembuat, String keterangan) {
        this.nomorPO = nomorPO;
        this.tanggalDibuat = tanggalDibuat;
        this.namaPembuat = namaPembuat;
        this.keterangan = keterangan;
        this.daftarItem = new POItem[50];
        this.jumlahItem = 0;
        this.statusPO = "OPEN";
        this.grandTotal = 0;
        this.riwayat = new String[50];
        this.jumlahRiwayat = 0;

        // Catat riwayat pertama saat PO dibuat
        tambahRiwayat("Order Created", tanggalDibuat, nomorPO);
    }

    // ACCESSOR
    public String getNomorPO() { return nomorPO; }
    public String getTanggalDibuat() { return tanggalDibuat; }
    public String getNamaPembuat() { return namaPembuat; }
    public POItem[] getDaftarItem() { return daftarItem; }
    public int getJumlahItem() { return jumlahItem; }
    public String getKeterangan() { return keterangan; }
    public String getStatusPO() { return statusPO; }
    public double getGrandTotal() { return grandTotal; }
    public String[] getRiwayat() { return riwayat; }
    public int getJumlahRiwayat() { return jumlahRiwayat; }

    // MUTATOR
    public void setNomorPO(String nomorPO) { this.nomorPO = nomorPO; }
    public void setTanggalDibuat(String tanggalDibuat) { this.tanggalDibuat = tanggalDibuat; }
    public void setNamaPembuat(String namaPembuat) { this.namaPembuat = namaPembuat; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
    public void setStatusPO(String statusPO) { this.statusPO = statusPO; }

    // TAMBAH ITEM KE PO (saat dibuat Buyer)
    public boolean tambahItem(POItem item) {
        // SELEKSI - cek kapasitas
        if (jumlahItem >= 50) {
            System.out.println("Maksimal 50 item per PO!");
            return false;
        }
        // SELEKSI - hanya bisa tambah item jika PO masih OPEN
        if (!statusPO.equals("OPEN")) {
            System.out.println("PO sudah diproses, tidak bisa tambah item!");
            return false;
        }
        daftarItem[jumlahItem] = item;
        jumlahItem++;
        hitungGrandTotal();
        return true;
    }

    // TAMBAH RIWAYAT BARU
    public void tambahRiwayat(String namaEvent, String tanggal, String nomorDokumen) {
        // SELEKSI - cek kapasitas riwayat
        if (jumlahRiwayat >= 50) {
            return; // diam saja kalau penuh, tidak fatal
        }
        String baris = String.format("%-22s | %-12s | %s", namaEvent, tanggal, nomorDokumen);
        riwayat[jumlahRiwayat] = baris;
        jumlahRiwayat++;
    }

    // CEK APAKAH MASIH ADA ITEM YANG BELUM DIKIRIM
    public boolean masihAdaItemBelumDikirim() {
        // PERULANGAN - cek semua item
        for (int i = 0; i < jumlahItem; i++) {
            if (daftarItem[i].getStatusItem().equals("BELUM DIKIRIM")) {
                return true;
            }
        }
        return false;
    }

    // CEK APAKAH SEMUA ITEM SUDAH DIKIRIM
    public boolean semuaItemSudahDikirim() {
        // PERULANGAN
        for (int i = 0; i < jumlahItem; i++) {
            if (daftarItem[i].getStatusItem().equals("BELUM DIKIRIM")) {
                return false;
            }
        }
        return true;
    }

    // UPDATE STATUS PO BERDASARKAN KONDISI KIRIM & TERIMA TERKINI
    // adaItemDikirim, semuaItemDikirim     -> dari kondisi POItem
    // adaDistribusiDiGR, semuaDistribusiDiGR -> dari GudangManager
    public void updateStatus(boolean adaItemDikirim, boolean semuaItemDikirim,
                              boolean adaDistribusiDiGR, boolean semuaDistribusiDiGR) {

        // SELEKSI - urutan prioritas dari kondisi tertinggi ke terendah
        if (semuaItemDikirim && semuaDistribusiDiGR && adaDistribusiDiGR) {
            statusPO = "RECEIVED";
        } else if (adaDistribusiDiGR) {
            statusPO = "PARTIALLY RECEIVED";
        } else if (semuaItemDikirim) {
            statusPO = "SHIPPED";
        } else if (adaItemDikirim) {
            statusPO = "PARTIALLY SHIPPED";
        } else {
            statusPO = "OPEN";
        }
    }

    // HITUNG GRAND TOTAL
    public void hitungGrandTotal() {
        grandTotal = 0;
        // PERULANGAN - jumlahkan semua total harga item
        for (int i = 0; i < jumlahItem; i++) {
            grandTotal += daftarItem[i].getTotalHarga();
        }
    }

    // TAMPIL RIWAYAT PO
    public void tampilRiwayat() {
        System.out.println("========================================================");
        System.out.println("  RIWAYAT PO");
        System.out.println("========================================================");
        // PERULANGAN - tampil semua riwayat
        for (int i = 0; i < jumlahRiwayat; i++) {
            System.out.println("  " + riwayat[i]);
        }
        System.out.println("========================================================");
    }

    // TAMPIL INFO PO
    public void tampilInfo() {
        System.out.println("========================================================");
        System.out.println("              PURCHASE ORDER");
        System.out.println("========================================================");
        System.out.printf("  No. PO       : %s%n", nomorPO);
        System.out.printf("  Tgl Dibuat   : %s%n", tanggalDibuat);
        System.out.printf("  Dibuat Oleh  : %s%n", namaPembuat);
        System.out.printf("  Status       : %s%n", statusPO);
        System.out.printf("  Keterangan   : %s%n", keterangan);
        System.out.println("--------------------------------------------------------");
        System.out.println("  DAFTAR ITEM");
        System.out.println("--------------------------------------------------------");
        // PERULANGAN - tampil semua item beserta status kirimnya
        for (int i = 0; i < jumlahItem; i++) {
            System.out.printf("  %d. ", (i + 1));
            daftarItem[i].tampilInfo();
        }
        System.out.println("--------------------------------------------------------");
        System.out.printf("  GRAND TOTAL  : Rp%.0f%n", grandTotal);
        System.out.println("========================================================");
        tampilRiwayat();
    }
}