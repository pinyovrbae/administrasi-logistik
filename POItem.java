package administrasilogistik;

public class POItem {
    // ATRIBUT
    private String namaBarang;
    private int quantity;
    private double hargaSatuan;
    private String jenis;        // EA, KG, UN, PCS, dll
    private double totalHarga;
    private String statusItem;   // BELUM DIKIRIM, SUDAH DIKIRIM

    // CONSTRUCTOR
    public POItem(String namaBarang, int quantity, double hargaSatuan, String jenis) {
        this.namaBarang = namaBarang;
        this.quantity = quantity;
        this.hargaSatuan = hargaSatuan;
        this.jenis = jenis;
        this.totalHarga = quantity * hargaSatuan;
        this.statusItem = "BELUM DIKIRIM";
    }

    // ACCESSOR
    public String getNamaBarang() { return namaBarang; }
    public int getQuantity() { return quantity; }
    public double getHargaSatuan() { return hargaSatuan; }
    public String getJenis() { return jenis; }
    public double getTotalHarga() { return totalHarga; }
    public String getStatusItem() { return statusItem; }

    // MUTATOR
    public void setNamaBarang(String namaBarang) { this.namaBarang = namaBarang; }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalHarga = quantity * hargaSatuan;
    }
    public void setHargaSatuan(double hargaSatuan) {
        this.hargaSatuan = hargaSatuan;
        this.totalHarga = quantity * hargaSatuan;
    }
    public void setJenis(String jenis) { this.jenis = jenis; }
    public void setStatusItem(String statusItem) { this.statusItem = statusItem; }

    public void tampilInfo() {
        System.out.printf("  %-20s | Qty:%-5d %-4s | Rp%-12.0f | Rp%-12.0f | %s%n",
                namaBarang, quantity, jenis, hargaSatuan, totalHarga, statusItem);
    }
}