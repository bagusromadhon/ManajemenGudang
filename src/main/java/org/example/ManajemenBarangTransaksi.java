package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Random;

public class ManajemenBarangTransaksi extends JFrame {

    private JTextField txtKodeBarangKeluar, txtStockKeluar, txtDescriptionKeluar, txtTanggalKeluar;
    private JTextField txtKodeBarangMasuk, txtStockMasuk, txtJumlahMasuk, txtDescriptionMasuk, txtTanggalMasuk;
    private DefaultTableModel tableModelBarangKeluar, tableModelBarangMasuk;
    private Connection connection;
    private JComboBox<Item> comboBarangMasuk, comboBarangKeluar;

    class Item {
        private int id;
        private String description;

        public Item(int id, String description) {
            this.id = id;
            this.description = description;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return description; // Ditampilkan di JComboBox
        }
    }

    public ManajemenBarangTransaksi() {
        setTitle("Transaksi Masuk dan Keluar Barang");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        connection = DatabaseSetUp.getConnection("jdbc:sqlite:warehouse.db");

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Barang Masuk", createBarangMasukPanel());
        tabbedPane.addTab("Barang Keluar", createBarangKeluarPanel());

        add(tabbedPane);

        loadComboBoxData(comboBarangMasuk);
        loadComboBoxData(comboBarangKeluar);

        loadBarangKeluar();

    }

    private void loadComboBoxData(JComboBox<Item> comboBox) {
        try {
            if (connection == null) {
                System.out.println("Koneksi database null di loadComboBoxData");
                return;
            }
            connection = DatabaseSetUp.getConnection("jdbc:sqlite:warehouse.db");

            String sql = "SELECT id_barang, nama_barang, stock_barang FROM barang";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            comboBox.removeAllItems();
            while (rs.next()) {
                int idBarang = rs.getInt("id_barang");
                String namaBarang = rs.getString("nama_barang");
                int stockBarang = rs.getInt("stock_barang");
                comboBox.addItem(new Item(idBarang, String.format("%s (Stock: %d)", namaBarang, stockBarang)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private JPanel createBarangKeluarPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtKodeBarangKeluar = createTextField("Kode Barang Keluar");
        txtStockKeluar = createTextField("Jumlah Barang Keluar");
        txtDescriptionKeluar = createTextField("Deskripsi Barang Keluar");
        comboBarangKeluar = new JComboBox<>();

        formPanel.add(new JLabel("Kode Barang Keluar:"));
        formPanel.add(txtKodeBarangKeluar);
        formPanel.add(new JLabel("Jumlah Barang Keluar:"));
        formPanel.add(txtStockKeluar);
        formPanel.add(new JLabel("Deskripsi Barang Keluar:"));
        formPanel.add(txtDescriptionKeluar);
        formPanel.add(new JLabel("Pilih Barang:"));
        formPanel.add(comboBarangKeluar);
        // Tambahkan ini ke formPanel
        // Tambahkan textfield untuk tanggal keluar
        txtTanggalKeluar = new JTextField();
        txtTanggalKeluar.setText(LocalDate.now().toString()); // Set tanggal saat ini
        txtTanggalKeluar.setEditable(false); // Nonaktifkan input manual
        formPanel.add(new JLabel("Tanggal Keluar:"));
        formPanel.add(txtTanggalKeluar);






        panel.add(formPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        JButton btnAdd = new JButton("Tambah");
        JButton btnLoad = new JButton("Muat Ulang");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnLoad);
        panel.add(buttonPanel, BorderLayout.CENTER);

        String[] columnNames = {"id_keluar", "id_barang", "Jumlah Keluar", "tanggal keluar", "stock terakhir","stock new","deskripsi"};
        tableModelBarangKeluar = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModelBarangKeluar);
        panel.add(new JScrollPane(table), BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> tambahBarangKeluar());
        btnLoad.addActionListener(e -> loadBarangKeluar());

        return panel;
    }

    private JPanel createBarangMasukPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtKodeBarangMasuk = createTextField("Kode Barang Keluar");
        txtStockMasuk = createTextField("Jumlah Barang Keluar");
        txtDescriptionMasuk = createTextField("Deskripsi Barang Keluar");
        comboBarangMasuk = new JComboBox<>();

        formPanel.add(new JLabel("Kode Barang Keluar:"));
        formPanel.add(txtKodeBarangMasuk);
        formPanel.add(new JLabel("Jumlah Barang Keluar:"));
        formPanel.add(txtStockMasuk);
        formPanel.add(new JLabel("Deskripsi Barang Keluar:"));
        formPanel.add(txtDescriptionMasuk);
        formPanel.add(new JLabel("Pilih Barang:"));
        formPanel.add(comboBarangMasuk);
        // Tambahkan ini ke formPanel
        // Tambahkan textfield untuk tanggal keluar
        txtTanggalMasuk = new JTextField();
        txtTanggalMasuk.setText(LocalDate.now().toString()); // Set tanggal saat ini
        txtTanggalMasuk.setEditable(false); // Nonaktifkan input manual
        formPanel.add(new JLabel("Tanggal Keluar:"));
        formPanel.add(txtTanggalMasuk);






        panel.add(formPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        JButton btnAdd = new JButton("Tambah");
        JButton btnLoad = new JButton("Muat Ulang");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnLoad);
        panel.add(buttonPanel, BorderLayout.CENTER);

        String[] columnNames = {"id_masuk", "id_barang", "Jumlah Masuk", "tanggal Masuk", "stock terakhir","stock new","deskripsi"};
        tableModelBarangMasuk = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModelBarangMasuk);
        panel.add(new JScrollPane(table), BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> tambahBarangMasuk());
        btnLoad.addActionListener(e -> loadBarangMasuk());

        return panel;
    }

    private JTextField createTextField(String placeholder) {
        JTextField textField = new JTextField();
        textField.setBorder(BorderFactory.createTitledBorder(placeholder));
        return textField;
    }
    private void tambahBarangKeluar() {
        try {
            Item selectedItem = (Item) comboBarangKeluar.getSelectedItem();
            if (selectedItem == null) {
                JOptionPane.showMessageDialog(this, "Pilih barang terlebih dahulu!");
                return;
            }

            int idBarang = selectedItem.getId();
            int stockTerakhir = getStockTerakhir(idBarang);
            int jumlahKeluar = Integer.parseInt(txtStockKeluar.getText());
            int stock_new = stockTerakhir - jumlahKeluar;
            String deskripsi = txtDescriptionKeluar.getText();

            if (stockTerakhir - jumlahKeluar < 0) {
                JOptionPane.showMessageDialog(this, "Stok tidak mencukupi!");
                return;
            }

            // Ambil tanggal keluar sebagai String
            String tanggalKeluar = txtTanggalKeluar.getText(); // Format YYYY-MM-DD

            // Insert data ke tabel barang_keluar
            String sqlInsert = "INSERT INTO barang_keluar (id_barang, jumlah_keluar, tanggal_keluar, stock_terakhir, stock_new, deskripsi) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmtInsert = connection.prepareStatement(sqlInsert);
            pstmtInsert.setInt(1, idBarang);
            pstmtInsert.setInt(2, jumlahKeluar);
            pstmtInsert.setString(3, tanggalKeluar); // Simpan sebagai teks
            pstmtInsert.setInt(4, stockTerakhir);
            pstmtInsert.setInt(5, stock_new);
            pstmtInsert.setString(6, deskripsi);
            pstmtInsert.executeUpdate();

            // Update stok pada tabel barang
            String sqlUpdate = "UPDATE barang SET stock_barang = ? WHERE id_barang = ?";
            PreparedStatement pstmtUpdate = connection.prepareStatement(sqlUpdate);
            pstmtUpdate.setInt(1, stock_new);
            pstmtUpdate.setInt(2, idBarang);
            pstmtUpdate.executeUpdate();

            JOptionPane.showMessageDialog(this, "Barang keluar berhasil ditambahkan dan stok barang diperbarui!");
            loadComboBoxData(comboBarangMasuk);
            loadComboBoxData(comboBarangKeluar);
            loadBarangKeluar(); // Reload data barang keluar untuk melihat hasil
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }



    private int getStockTerakhir(int idBarang) {
        try {
            String sql = "SELECT stock_barang FROM barang WHERE id_barang = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, idBarang);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("stock_barang");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
        return 0;
    }

    private void tambahBarangMasuk() {
        try {
            Item selectedItem = (Item) comboBarangMasuk.getSelectedItem();
            if (selectedItem == null) {
                JOptionPane.showMessageDialog(this, "Pilih barang terlebih dahulu!");
                return;
            }

            int idBarang = selectedItem.getId();
            int stockTerakhir = getStockTerakhir(idBarang);
            int jumlahMasuk = Integer.parseInt(txtStockMasuk.getText());
            int stock_new = stockTerakhir + jumlahMasuk;
            String deskripsi = txtDescriptionMasuk.getText();

//            if (stockTerakhir - jumlahKeluar < 0) {
//                JOptionPane.showMessageDialog(this, "Stok tidak mencukupi!");
//                return;
//            }

            // Ambil tanggal keluar sebagai String
            String tanggalMasuk = txtTanggalKeluar.getText(); // Format YYYY-MM-DD

            // Insert data ke tabel barang_keluar
            String sqlInsert = "INSERT INTO barang_masuk (id_barang, jumlah_masuk, tanggal_masuk, stock_terakhir, stock_new, deskripsi) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmtInsert = connection.prepareStatement(sqlInsert);
            pstmtInsert.setInt(1, idBarang);
            pstmtInsert.setInt(2, jumlahMasuk);
            pstmtInsert.setString(3, tanggalMasuk); // Simpan sebagai teks
            pstmtInsert.setInt(4, stockTerakhir);
            pstmtInsert.setInt(5, stock_new);
            pstmtInsert.setString(6, deskripsi);
            pstmtInsert.executeUpdate();

            // Update stok pada tabel barang
            String sqlUpdate = "UPDATE barang SET stock_barang = ? WHERE id_barang = ?";
            PreparedStatement pstmtUpdate = connection.prepareStatement(sqlUpdate);
            pstmtUpdate.setInt(1, stock_new);
            pstmtUpdate.setInt(2, idBarang);
            pstmtUpdate.executeUpdate();

            JOptionPane.showMessageDialog(this, "Barang Masuk berhasil ditambahkan dan stok barang diperbarui!");
            loadComboBoxData(comboBarangMasuk);
            loadComboBoxData(comboBarangKeluar);
            loadBarangMasuk(); // Reload data barang keluar untuk melihat hasil
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void loadBarangKeluar() {
        try {
            String sql = "SELECT * FROM barang_keluar";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            tableModelBarangKeluar.setRowCount(0);
            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id_keluar"),
                        rs.getInt("id_barang"),
                        rs.getInt("jumlah_keluar"),
                        rs.getString("tanggal_keluar"),
                        rs.getInt("stock_terakhir"),
                        rs.getInt("stock_new"),
                        rs.getString("deskripsi")
                };
                tableModelBarangKeluar.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void loadBarangMasuk() {
        try {
            String sql = "SELECT * FROM barang_masuk";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            tableModelBarangMasuk.setRowCount(0);
            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id_masuk"),
                        rs.getInt("id_barang"),
                        rs.getInt("jumlah_masuk"),
                        rs.getString("tanggal_masuk"),
                        rs.getInt("stock_terakhir"),
                        rs.getInt("stock_new"),
                        rs.getString("deskripsi")
                };
                tableModelBarangMasuk.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ManajemenBarangTransaksi().setVisible(true);
        });
    }
}
