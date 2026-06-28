package administrasilogistik;

public class GudangManager {
    // ARRAY - menyimpan semua data
    private PurchaseOrder[] daftarPO;
    private Distribusi[] daftarDistribusi;
    private GoodReceipt[] daftarGR;
    private Invoice[] daftarInvoice;

    private int jumlahPO;
    private int jumlahDistribusi;
    private int jumlahGR;
    private int jumlahInvoice;

    // CONSTRUCTOR
    public GudangManager() {
        this.daftarPO = new PurchaseOrder[100];
        this.daftarDistribusi = new Distribusi[100];
        this.daftarGR = new GoodReceipt[100];
        this.daftarInvoice = new Invoice[100];
        this.jumlahPO = 0;
        this.jumlahDistribusi = 0;
        this.jumlahGR = 0;
        this.jumlahInvoice = 0;
    }

    // ===== ACCESSOR =====
    public PurchaseOrder[] getDaftarPO() { return daftarPO; }
    public Distribusi[] getDaftarDistribusi() { return daftarDistribusi; }
    public GoodReceipt[] getDaftarGR() { return daftarGR; }
    public Invoice[] getDaftarInvoice() { return daftarInvoice; }
    public int getJumlahPO() { return jumlahPO; }
    public int getJumlahDistribusi() { return jumlahDistribusi; }
    public int getJumlahGR() { return jumlahGR; }
    public int getJumlahInvoice() { return jumlahInvoice; }

    // ===== SIMPAN PO =====
    public boolean simpanPO(PurchaseOrder po) {
        // SELEKSI - cek kapasitas
        if (jumlahPO >= 100) {
            System.out.println("❌ Kapasitas PO penuh!");
            return false;
        }
        // SELEKSI - cek nomor PO duplikat
        for (int i = 0; i < jumlahPO; i++) {
            if (daftarPO[i].getNomorPO().equals(po.getNomorPO())) {
                System.out.println("❌ Nomor PO " + po.getNomorPO() + " sudah ada!");
                return false;
            }
        }
        daftarPO[jumlahPO] = po;
        jumlahPO++;
        System.out.println("✅ PO " + po.getNomorPO() + " berhasil disimpan!");
        return true;
    }

    // ===== CARI PO BY NOMOR =====
    public PurchaseOrder cariPO(String nomorPO) {
        // PERULANGAN - cari di seluruh array
        for (int i = 0; i < jumlahPO; i++) {
            if (daftarPO[i].getNomorPO().equalsIgnoreCase(nomorPO)) {
                return daftarPO[i];
            }
        }
        return null;
    }

    // ===== SIMPAN DISTRIBUSI =====
    public boolean simpanDistribusi(Distribusi dist) {
        // SELEKSI - cek kapasitas
        if (jumlahDistribusi >= 100) {
            System.out.println("❌ Kapasitas Distribusi penuh!");
            return false;
        }
        // SELEKSI - cek nomor distribusi duplikat
        for (int i = 0; i < jumlahDistribusi; i++) {
            if (daftarDistribusi[i].getNomorDistribusi()
                    .equals(dist.getNomorDistribusi())) {
                System.out.println("❌ Nomor Distribusi sudah ada!");
                return false;
            }
        }
        daftarDistribusi[jumlahDistribusi] = dist;
        jumlahDistribusi++;
        System.out.println("✅ Distribusi " + dist.getNomorDistribusi() + " berhasil disimpan!");
        return true;
    }

    // ===== CARI DISTRIBUSI BY NOMOR DISTRIBUSI =====
    public Distribusi cariDistribusi(String nomorDistribusi) {
        // PERULANGAN - cari di seluruh array
        for (int i = 0; i < jumlahDistribusi; i++) {
            if (daftarDistribusi[i].getNomorDistribusi().equalsIgnoreCase(nomorDistribusi)) {
                return daftarDistribusi[i];
            }
        }
        return null;
    }

    // ===== CARI SEMUA DISTRIBUSI UNTUK 1 PO =====
    public Distribusi[] cariSemuaDistribusiByPO(String nomorPO) {
        Distribusi[] hasil = new Distribusi[jumlahDistribusi];
        int count = 0;
        // PERULANGAN - kumpulkan semua distribusi milik PO ini
        for (int i = 0; i < jumlahDistribusi; i++) {
            if (daftarDistribusi[i].getNomorPO().equalsIgnoreCase(nomorPO)) {
                hasil[count] = daftarDistribusi[i];
                count++;
            }
        }
        return hasil;
    }

    // ===== HITUNG JUMLAH DISTRIBUSI UNTUK 1 PO =====
    public int hitungJumlahDistribusiByPO(String nomorPO) {
        int count = 0;
        // PERULANGAN
        for (int i = 0; i < jumlahDistribusi; i++) {
            if (daftarDistribusi[i].getNomorPO().equalsIgnoreCase(nomorPO)) {
                count++;
            }
        }
        return count;
    }

    // ===== CARI DISTRIBUSI YANG BELUM DI-GR UNTUK 1 PO =====
    public Distribusi[] cariDistribusiBelumGRByPO(String nomorPO) {
        Distribusi[] hasil = new Distribusi[jumlahDistribusi];
        int count = 0;
        // PERULANGAN - filter distribusi milik PO ini yang statusnya masih "DIKIRIM"
        for (int i = 0; i < jumlahDistribusi; i++) {
            if (daftarDistribusi[i].getNomorPO().equalsIgnoreCase(nomorPO)
                    && daftarDistribusi[i].getStatusDistribusi().equals("DIKIRIM")) {
                hasil[count] = daftarDistribusi[i];
                count++;
            }
        }
        return hasil;
    }

    // ===== CEK APAKAH SEMUA DISTRIBUSI MILIK PO INI SUDAH DI-GR =====
    public boolean semuaDistribusiSudahGR(String nomorPO) {
        // PERULANGAN - cek tiap distribusi milik PO ini
        for (int i = 0; i < jumlahDistribusi; i++) {
            if (daftarDistribusi[i].getNomorPO().equalsIgnoreCase(nomorPO)
                    && daftarDistribusi[i].getStatusDistribusi().equals("DIKIRIM")) {
                return false; // masih ada yang belum GR
            }
        }
        return true; // semua sudah GR
    }
    
    // ===== CEK APAKAH ADA MINIMAL 1 DISTRIBUSI YANG SUDAH DI-GR =====
    public boolean adaDistribusiSudahGR(String nomorPO) {
        // PERULANGAN - cek tiap distribusi milik PO ini
        for (int i = 0; i < jumlahDistribusi; i++) {
            if (daftarDistribusi[i].getNomorPO().equalsIgnoreCase(nomorPO)
                    && daftarDistribusi[i].getStatusDistribusi().equals("GR DONE")) {
                return true; // ada minimal 1 distribusi yang sudah di-GR
            }
        }
        return false;
    }

    // ===== SIMPAN GOOD RECEIPT =====
    public boolean simpanGR(GoodReceipt gr) {
        // SELEKSI - cek kapasitas
        if (jumlahGR >= 100) {
            System.out.println("❌ Kapasitas GR penuh!");
            return false;
        }
        // SELEKSI - cek nomor GR duplikat
        for (int i = 0; i < jumlahGR; i++) {
            if (daftarGR[i].getNomorGR().equals(gr.getNomorGR())) {
                System.out.println("❌ Nomor GR " + gr.getNomorGR() + " sudah ada!");
                return false;
            }
        }
        daftarGR[jumlahGR] = gr;
        jumlahGR++;
        System.out.println("✅ Good Receipt " + gr.getNomorGR() + " berhasil disimpan!");
        return true;
    }

    // ===== CARI SEMUA GR UNTUK 1 PO =====
    public GoodReceipt[] cariSemuaGRByPO(String nomorPO) {
        GoodReceipt[] hasil = new GoodReceipt[jumlahGR];
        int count = 0;
        // PERULANGAN - kumpulkan semua GR milik PO ini
        for (int i = 0; i < jumlahGR; i++) {
            if (daftarGR[i].getNomorPO().equalsIgnoreCase(nomorPO)) {
                hasil[count] = daftarGR[i];
                count++;
            }
        }
        return hasil;
    }

    // ===== SIMPAN INVOICE =====
    public boolean simpanInvoice(Invoice invoice) {
        // SELEKSI - cek kapasitas
        if (jumlahInvoice >= 100) {
            System.out.println("❌ Kapasitas Invoice penuh!");
            return false;
        }
        // SELEKSI - cek nomor invoice duplikat
        for (int i = 0; i < jumlahInvoice; i++) {
            if (daftarInvoice[i].getNomorInvoice().equals(invoice.getNomorInvoice())) {
                System.out.println("❌ Nomor Invoice " + invoice.getNomorInvoice() + " sudah ada!");
                return false;
            }
        }
        // SELEKSI - cek invoice untuk PO ini sudah ada
        for (int i = 0; i < jumlahInvoice; i++) {
            if (daftarInvoice[i].getNomorPO().equals(invoice.getNomorPO())) {
                System.out.println("❌ Invoice untuk PO " + invoice.getNomorPO() + " sudah dibuat!");
                return false;
            }
        }
        daftarInvoice[jumlahInvoice] = invoice;
        jumlahInvoice++;
        System.out.println("✅ Invoice " + invoice.getNomorInvoice() + " berhasil disimpan!");
        return true;
    }

    // ===== CARI INVOICE BY NOMOR INVOICE =====
    public Invoice cariInvoice(String nomorInvoice) {
        // PERULANGAN - cari di seluruh array
        for (int i = 0; i < jumlahInvoice; i++) {
            if (daftarInvoice[i].getNomorInvoice().equalsIgnoreCase(nomorInvoice)) {
                return daftarInvoice[i];
            }
        }
        return null;
    }

    // ===== CARI INVOICE BY NOMOR PO =====
    public Invoice cariInvoiceByPO(String nomorPO) {
        for (int i = 0; i < jumlahInvoice; i++) {
            if (daftarInvoice[i].getNomorPO().equalsIgnoreCase(nomorPO)) {
                return daftarInvoice[i];
            }
        }
        return null;
    }

    // ===== TAMPIL SEMUA PO =====
    public void tampilSemuaPO() {
        System.out.println("========================================================");
        System.out.println("=              DAFTAR PURCHASE ORDER                   =");
        System.out.println("========================================================");
        if (jumlahPO == 0) {
            System.out.println("=  Belum ada PO.                                   =");
            System.out.println("====================================================");
            return;
        }
        System.out.printf("  %-3s %-15s %-12s %-15s %-18s%n",
                "No.", "Nomor PO", "Tgl Dibuat", "Dibuat Oleh", "Status");
        System.out.println("  ---------------------------------------------------------------");
        // PERULANGAN - tampil semua PO
        for (int i = 0; i < jumlahPO; i++) {
            PurchaseOrder po = daftarPO[i];
            System.out.printf("  %-3d %-15s %-12s %-15s %-18s%n",
                    (i + 1),
                    po.getNomorPO(),
                    po.getTanggalDibuat(),
                    po.getNamaPembuat(),
                    po.getStatusPO());
        }
        System.out.println("========================================================");
    }

    // ===== TAMPIL SEMUA GR =====
    public void tampilSemuaGR() {
        System.out.println("========================================================");
        System.out.println("=              DAFTAR GOOD RECEIPT                     =");
        System.out.println("========================================================");
        if (jumlahGR == 0) {
            System.out.println("=  Belum ada Good Receipt.                             =");
            System.out.println("========================================================");
            return;
        }
        System.out.printf("  %-3s %-12s %-15s %-12s %-15s%n",
                "No.", "Nomor GR", "Nomor PO", "Tgl Terima", "Diterima Oleh");
        System.out.println("  ---------------------------------------------------------------");
        // PERULANGAN - tampil semua GR
        for (int i = 0; i < jumlahGR; i++) {
            GoodReceipt gr = daftarGR[i];
            System.out.printf("  %-3d %-12s %-15s %-12s %-15s%n",
                    (i + 1),
                    gr.getNomorGR(),
                    gr.getNomorPO(),
                    gr.getTanggalTerima(),
                    gr.getNamaPenerima());
        }
        System.out.println("========================================================");
    }

    // ===== TAMPIL SEMUA INVOICE =====
    public void tampilSemuaInvoice() {
        System.out.println("========================================================");
        System.out.println("=              DAFTAR INVOICE                          =");
        System.out.println("========================================================");
        if (jumlahInvoice == 0) {
            System.out.println("=  Belum ada Invoice.                                  =");
            System.out.println("========================================================");
            return;
        }
        System.out.printf("  %-3s %-15s %-15s %-15s %-10s%n",
                "No.", "No. Invoice", "No. PO", "Total Tagihan", "Status");
        System.out.println("  ---------------------------------------------------------------");
        // PERULANGAN - tampil semua invoice
        for (int i = 0; i < jumlahInvoice; i++) {
            Invoice inv = daftarInvoice[i];
            System.out.printf("  %-3d %-15s %-15s Rp%-13.0f %-10s%n",
                    (i + 1),
                    inv.getNomorInvoice(),
                    inv.getNomorPO(),
                    inv.getTotalTagihan(),
                    inv.getStatusInvoice());
        }
        System.out.println("========================================================");
    }

    // ===== TAMPIL INVOICE UNPAID =====
    public Invoice[] getInvoiceUnpaid() {
        Invoice[] unpaid = new Invoice[jumlahInvoice];
        int count = 0;
        // PERULANGAN - filter hanya yang UNPAID
        for (int i = 0; i < jumlahInvoice; i++) {
            if (daftarInvoice[i].getStatusInvoice().equals("UNPAID")) {
                unpaid[count] = daftarInvoice[i];
                count++;
            }
        }
        return unpaid;
    }

    // ===== HITUNG TOTAL UNPAID =====
    public double hitungTotalUnpaid() {
        double total = 0;
        // PERULANGAN - jumlahkan semua tagihan UNPAID
        for (int i = 0; i < jumlahInvoice; i++) {
            if (daftarInvoice[i].getStatusInvoice().equals("UNPAID")) {
                total += daftarInvoice[i].getTotalTagihan();
            }
        }
        return total;
    }
}