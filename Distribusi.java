package administrasilogistik;

public class Distribusi {
    // ATRIBUT
    private String nomorDistribusi;
    private String nomorPO;
    private String tanggalPengiriman;
    private String namaSuplier;
    private String statusDistribusi;     // DIKIRIM, GR DONE
    private DistribusiItem[] daftarItem; // ARRAY rincian barang yang dikirim KALI INI
    private int jumlahItem;

    // CONSTRUCTOR
    public Distribusi(String nomorDistribusi, String nomorPO,
                      String tanggalPengiriman, String namaSupplier) {
        this.nomorDistribusi = nomorDistribusi;
        this.nomorPO = nomorPO;
        this.tanggalPengiriman = tanggalPengiriman;
        this.namaSuplier = namaSupplier;
        this.statusDistribusi = "DIKIRIM";
        this.daftarItem = new DistribusiItem[50];
        this.jumlahItem = 0;
    }

    // ACCESSOR
    public String getNomorDistribusi() { return nomorDistribusi; }
    public String getNomorPO() { return nomorPO; }
    public String getTanggalPengiriman() { return tanggalPengiriman; }
    public String getNamaSupplier() { return namaSuplier; }
    public String getStatusDistribusi() { return statusDistribusi; }
    public DistribusiItem[] getDaftarItem() { return daftarItem; }
    public int getJumlahItem() { return jumlahItem; }

    // MUTATOR
    public void setNomorDistribusi(String nomorDistribusi) { this.nomorDistribusi = nomorDistribusi; }
    public void setNomorPO(String nomorPO) { this.nomorPO = nomorPO; }
    public void setTanggalPengiriman(String tanggalPengiriman) { this.tanggalPengiriman = tanggalPengiriman; }
    public void setNamaSupplier(String namaSupplier) { this.namaSuplier = namaSupplier; }
    public void setStatusDistribusi(String statusDistribusi) { this.statusDistribusi = statusDistribusi; }

    // TAMBAH ITEM YANG DIKIRIM
    public boolean tambahItem(DistribusiItem item) {
        // SELEKSI - cek kapasitas
        if (jumlahItem >= 50) {
            System.out.println("❌ Maksimal 50 item per distribusi!");
            return false;
        }
        daftarItem[jumlahItem] = item;
        jumlahItem++;
        return true;
    }

    // TAMPIL INFO
    public void tampilInfo() {
        System.out.println("========================================================");
        System.out.println("=                  DATA DISTRIBUSI                     =");
        System.out.println("========================================================");
        System.out.printf("=  No. Distribusi  : %-33s║%n", nomorDistribusi);
        System.out.printf("=  No. PO          : %-33s║%n", nomorPO);
        System.out.printf("=  Nama Supplier   : %-33s║%n", namaSuplier);
        System.out.printf("=  Tgl Pengiriman  : %-33s║%n", tanggalPengiriman);
        System.out.printf("=  Status          : %-33s║%n", statusDistribusi);
        System.out.println("========================================================");
        System.out.println("=                ITEM YANG DIKIRIM                     =");
        System.out.println("========================================================");
        // PERULANGAN - tampil semua item yang dikirim di distribusi ini
        if (jumlahItem == 0) {
            System.out.println("  (Tidak ada item)");
        } else {
            for (int i = 0; i < jumlahItem; i++) {
                daftarItem[i].tampilInfo();
            }
        }
        System.out.println("========================================================");
    }
}