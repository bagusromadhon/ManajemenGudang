package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetUp {

    private static Connection connection;

    public static void initializeDatabase() {
        try {
            // Inisialisasi koneksi database SQLite
            connection = DriverManager.getConnection("jdbc:sqlite:warehouse.db");

            Statement stmt = connection.createStatement();

            // Buat tabel barang
            String createBarangTable = "CREATE TABLE IF NOT EXISTS barang (" +
                    "id_barang INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nama_barang TEXT," +
                    "kategori TEXT," +
                    "stock_barang INTEGER)";
            stmt.execute(createBarangTable);

            // Buat tabel barang_keluar
            String createBarangKeluarTable = "CREATE TABLE IF NOT EXISTS barang_keluar (" +
                    "id_keluar INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "id_barang INTEGER," +
                    "jumlah_keluar INTEGER," +
                    "tanggal_keluar DATE," +
                    "stock_terakhir INTEGER," +
                    "stock_new INTEGER," +
                    "deskripsi TEXT," +
                    "FOREIGN KEY(id_barang) REFERENCES barang(id_barang))";
            stmt.execute(createBarangKeluarTable);

            // Buat tabel barang_masuk
            String createBarangMasukTable = "CREATE TABLE IF NOT EXISTS barang_masuk (" +
                    "id_masuk INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "id_barang INTEGER," +
                    "jumlah_masuk INTEGER," +
                    "tanggal_masuk DATE," +
                    "stock_terakhir INTEGER," +
                    "stock_new INTEGER," +
                    "deskripsi TEXT," +
                    "FOREIGN KEY(id_barang) REFERENCES barang(id_barang))";
            stmt.execute(createBarangMasukTable);

            //Buat tabel Login
            String sqlAccount = "CREATE TABLE IF NOT EXISTS account (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT NOT NULL, " +
                    "password TEXT NOT NULL)";
            stmt.execute(sqlAccount);

            // Insert default account (if needed)
            String sqlInsert = "INSERT OR IGNORE INTO account (username, password) " +
                    "VALUES ('admin', 'admin123')";
            stmt.execute(sqlInsert);


            System.out.println("Database initialized successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection(String url) {
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null; // Pastikan Anda menangani nilai null saat memanggil metode ini
        }
    }
}
