package administrasilogistik;

public class DistribusiItem {
    // ATRIBUT
    private String namaBarang;
    private int quantity;
    private String jenis;

    // CONSTRUCTOR
    public DistribusiItem(String namaBarang, int quantity, String jenis) {
        this.namaBarang = namaBarang;
        this.quantity = quantity;
        this.jenis = jenis;
    }

    // ACCESSOR
    public String getNamaBarang() { return namaBarang; }
    public int getQuantity() { return quantity; }
    public String getJenis() { return jenis; }

    // MUTATOR
    public void setNamaBarang(String namaBarang) { this.namaBarang = namaBarang; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setJenis(String jenis) { this.jenis = jenis; }

    public void tampilInfo() {
        System.out.printf("  %-20s | Qty: %-5d %s%n", namaBarang, quantity, jenis);
    }
}