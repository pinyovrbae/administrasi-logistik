package administrasilogistik;

import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static GudangManager manager = new GudangManager();
    static Buyer buyer = new Buyer("Buyer", manager);
    static Supplier supplier = new Supplier("Supplier", manager);

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            System.out.println("=================================================");
            System.out.println("=       SISTEM ADMINISTRASI & LOGISTIK          =");
            System.out.println("=================================================");
            System.out.println("=  Login sebagai:                               =");
            System.out.println("=  1. Buyer                                     =");
            System.out.println("=  2. Supplier                                  =");
            System.out.println("=  0. Keluar                                    =");
            System.out.println("=================================================");
            System.out.print("Pilih: ");

            try {
                int pilihan = Integer.parseInt(scanner.nextLine());
                switch (pilihan) {
                    case 1 -> {
                        System.out.print("\nMasukkan Nama Anda: ");
                        String namaBuyer = scanner.nextLine();
                        buyer.setNama(namaBuyer);
                        menuBuyer();
                    }
                    case 2 -> {
                        System.out.print("\nMasukkan Nama Anda: ");
                        String namaSupplier = scanner.nextLine();
                        supplier.setNama(namaSupplier);
                        menuSupplier();
                    }
                    case 0 -> {
                        System.out.println("\nTerima kasih! Program selesai.");
                        running = false;
                    }
                    default -> System.out.println("Pilihan tidak valid!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Masukkan angka yang valid!");
            }
        }
    }

    static void menuBuyer() {
        boolean back = false;
        while (!back) {
            System.out.println("=================================================");
            System.out.printf ("=    Buyer: %-42s║%n", buyer.getNama());
            System.out.println("=================================================");
            System.out.println("=  1. Buat Purchase Order (PO)                  =");
            System.out.println("=  2. Lihat Semua PO                            =");
            System.out.println("=  3. Cek Detail PO                             =");
            System.out.println("=  4. Buat Good Receipt (GR)                    =");
            System.out.println("=  5. Lihat Semua Good Receipt                  =");
            System.out.println("=  6. Cek Invoice                               =");
            System.out.println("=  7. Bayar Invoice                             =");
            System.out.println("=  0. Kembali ke Menu Utama                     =");
            System.out.println("=================================================");
            System.out.print("Pilih: ");

            try {
                int pilihan = Integer.parseInt(scanner.nextLine());
                switch (pilihan) {
                    case 1 -> buyer.buatPO();
                    case 2 -> buyer.lihatSemuaPO();
                    case 3 -> buyer.cekDetailPO();
                    case 4 -> buyer.buatGoodReceipt();
                    case 5 -> buyer.lihatSemuaGR();
                    case 6 -> buyer.cekInvoice();
                    case 7 -> buyer.bayarInvoice();
                    case 0 -> back = true;
                    default -> System.out.println("Pilihan tidak valid!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Masukkan angka yang valid!");
            }
        }
    }

    static void menuSupplier() {
    boolean back = false;
    while (!back) {
        System.out.println("\n========================================================");
        System.out.printf ("  Supplier: %s%n", supplier.getNama());
        System.out.println("========================================================");
        System.out.println("  1. Proses Purchase Order (PO)");
        System.out.println("  2. Lihat Semua PO");
        System.out.println("  3. Cek Detail PO");
        System.out.println("  4. Buat Invoice");
        System.out.println("  5. Lihat Semua Invoice");
        System.out.println("  0. Kembali ke Menu Utama");
        System.out.println("========================================================");
        System.out.print("Pilih: ");

        try {
            int pilihan = Integer.parseInt(scanner.nextLine());
            switch (pilihan) {
                case 1 -> supplier.prosesPO();
                case 2 -> supplier.lihatSemuaPO();
                case 3 -> supplier.cekDetailPO();
                case 4 -> supplier.buatInvoice();
                case 5 -> supplier.lihatSemuaInvoice();
                case 0 -> back = true;
                default -> System.out.println("Pilihan tidak valid!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Masukkan angka yang valid!");
        }
    }
}}
