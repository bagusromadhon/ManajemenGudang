package org.example;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestSQLiteConnection {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:test.db"; // Nama file database

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("Koneksi SQLite berhasil!");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
