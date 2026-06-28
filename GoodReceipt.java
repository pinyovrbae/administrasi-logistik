package administrasilogistik;

public class GoodReceipt {
    // Atribut
    private String nomorGR;
    private String nomorPO;
    private String nomorDistribusi;
    private String tanggalTerima;
    private String namaPenerima;
    private String catatan;
    private String statusGR;        

    // Constructor
    public GoodReceipt(String nomorGR, String nomorPO,
                       String nomorDistribusi, String tanggalTerima,
                       String namaPenerima, String catatan) {
        this.nomorGR = nomorGR;
        this.nomorPO = nomorPO;
        this.nomorDistribusi = nomorDistribusi;
        this.tanggalTerima = tanggalTerima;
        this.namaPenerima = namaPenerima;
        this.catatan = catatan;
        this.statusGR = "DITERIMA";
    }

    // Accessor
    public String getNomorGR() { return nomorGR; }
    public String getNomorPO() { return nomorPO; }
    public String getNomorDistribusi() { return nomorDistribusi; }
    public String getTanggalTerima() { return tanggalTerima; }
    public String getNamaPenerima() { return namaPenerima; }
    public String getCatatan() { return catatan; }
    public String getStatusGR() { return statusGR; }

    // Mutator
    public void setNomorGR(String nomorGR) { this.nomorGR = nomorGR; }
    public void setNomorPO(String nomorPO) { this.nomorPO = nomorPO; }
    public void setNomorDistribusi(String nomorDistribusi) { this.nomorDistribusi = nomorDistribusi; }
    public void setTanggalTerima(String tanggalTerima) { this.tanggalTerima = tanggalTerima; }
    public void setNamaPenerima(String namaPenerima) { this.namaPenerima = namaPenerima; }
    public void setCatatan(String catatan) { this.catatan = catatan; }
    public void setStatusGR(String statusGR) { this.statusGR = statusGR; }

    // Tampil Info
    public void tampilInfo() {
        System.out.println("=================================================");
        System.out.println("=              GOOD RECEIPT                     =");
        System.out.println("=================================================");
        System.out.printf("=  No. GR          : %-33s║%n", nomorGR);
        System.out.printf("=  No. PO          : %-33s║%n", nomorPO);
        System.out.printf("=  No. Distribusi  : %-33s║%n", nomorDistribusi);
        System.out.printf("=  Tgl Terima      : %-33s║%n", tanggalTerima);
        System.out.printf("=  Diterima Oleh   : %-33s║%n", namaPenerima);
        System.out.printf("=  Catatan         : %-33s║%n", catatan);
        System.out.printf("=  Status          : %-33s║%n", statusGR);
        System.out.println("=================================================");
    }
}