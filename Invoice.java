package administrasilogistik;

public class Invoice {
    // Atribut
    private String nomorInvoice;
    private String nomorPO;
    private String nomorGR;
    private String namaSupplier;
    private String tanggalInvoice;
    private double totalTagihan;
    private String statusInvoice;   // UNPAID, PAID
    private String tanggalBayar;
    private String namaPembayar;

    // Constructor
    public Invoice(String nomorInvoice, String nomorPO, String nomorGR,
                   String namaSupplier, String tanggalInvoice, double totalTagihan) {
        this.nomorInvoice = nomorInvoice;
        this.nomorPO = nomorPO;
        this.nomorGR = nomorGR;
        this.namaSupplier = namaSupplier;
        this.tanggalInvoice = tanggalInvoice;
        this.totalTagihan = totalTagihan;
        this.statusInvoice = "UNPAID";
        this.tanggalBayar = "-";
        this.namaPembayar = "-";
    }

    // Accessor
    public String getNomorInvoice() { return nomorInvoice; }
    public String getNomorPO() { return nomorPO; }
    public String getNomorGR() { return nomorGR; }
    public String getNamaSupplier() { return namaSupplier; }
    public String getTanggalInvoice() { return tanggalInvoice; }
    public double getTotalTagihan() { return totalTagihan; }
    public String getStatusInvoice() { return statusInvoice; }
    public String getTanggalBayar() { return tanggalBayar; }
    public String getNamaPembayar() { return namaPembayar; }

    // Mutator
    public void setNomorInvoice(String nomorInvoice) { this.nomorInvoice = nomorInvoice; }
    public void setNomorPO(String nomorPO) { this.nomorPO = nomorPO; }
    public void setNomorGR(String nomorGR) { this.nomorGR = nomorGR; }
    public void setNamaSupplier(String namaSupplier) { this.namaSupplier = namaSupplier; }
    public void setTanggalInvoice(String tanggalInvoice) { this.tanggalInvoice = tanggalInvoice; }
    public void setTotalTagihan(double totalTagihan) { this.totalTagihan = totalTagihan; }
    public void setStatusInvoice(String statusInvoice) { this.statusInvoice = statusInvoice; }
    public void setTanggalBayar(String tanggalBayar) { this.tanggalBayar = tanggalBayar; }
    public void setNamaPembayar(String namaPembayar) { this.namaPembayar = namaPembayar; }

    public void tampilInfo() {
        System.out.println("=================================================");
        System.out.println("=                    INVOICE                    =");
        System.out.println("=================================================");
        System.out.printf("=  No. Invoice     : %-33s║%n", nomorInvoice);
        System.out.printf("=  No. PO          : %-33s║%n", nomorPO);
        System.out.printf("=  No. GR          : %-33s║%n", nomorGR);
        System.out.printf("=  Nama Supplier   : %-33s║%n", namaSupplier);
        System.out.printf("=  Tgl Invoice     : %-33s║%n", tanggalInvoice);
        System.out.printf("=  Total Tagihan   : Rp%-31.0f║%n", totalTagihan);
        System.out.printf("=  Status          : %-33s║%n", statusInvoice);
        System.out.printf("=  Tgl Bayar       : %-33s║%n", tanggalBayar);
        System.out.printf("=  Dibayar Oleh    : %-33s║%n", namaPembayar);
        System.out.println("=================================================");
    }
}