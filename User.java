package administrasilogistik;

public class User {
    // Atribut
    private String nama;
    private String role;

    // Constructor
    public User(String nama, String role) {
        this.nama = nama;
        this.role = role;
    }

    // Accessor
    public String getNama() { return nama; }
    public String getRole() { return role; }

    // Mutator
    public void setNama(String nama) { this.nama = nama; }
    public void setRole(String role) { this.role = role; }

    // TAMPIL PROFIL @override oleh Buyer & Supplier
    public void tampilProfil() {
        System.out.println("========================================================");
        System.out.println("  PROFIL PENGGUNA");
        System.out.println("========================================================");
        System.out.printf("  Nama : %s%n", nama);
        System.out.printf("  Role : %s%n", role);
    }
}