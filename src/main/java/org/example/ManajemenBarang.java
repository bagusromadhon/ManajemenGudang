package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class ManajemenBarang extends JFrame {
    private JTextField txtNama, txtKategori, txtStock;
    private DefaultTableModel tableModel;
    private JTable table;
    private Connection connection;

    public ManajemenBarang() {
        setTitle("Manajemen Barang - Database Integration");
        setSize(800, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Database Setup
//        setupDatabase();

        DatabaseSetUp.initializeDatabase();
        connection = DatabaseSetUp.getConnection("jdbc:sqlite:warehouse.db");
//            Statement stmt = connection.createStatement();

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(58, 134, 255));
        JLabel headerLabel = new JLabel("Manajemen Barang");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Form Input
        JPanel formPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);

        txtNama = createTextField("Nama Barang");
        txtKategori = createTextField("Kategori Barang");
        txtStock = createTextField("Stock Barang");

        formPanel.add(new JLabel("Nama Barang:"));
        formPanel.add(txtNama);
        formPanel.add(new JLabel("Kategori:"));
        formPanel.add(txtKategori);
        formPanel.add(new JLabel("Stock:"));
        formPanel.add(txtStock);

        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        JButton btnAdd = new JButton("Tambah");
        JButton btnLoad = new JButton("Muat Ulang");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Hapus");
        JButton btnClear = new JButton("Clear");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnLoad);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        add(buttonPanel, BorderLayout.SOUTH);

        // Tabel Barang
        String[] columnNames = {"ID Barang", "Nama Barang", "Kategori", "Stock"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(table);
        add(tableScroll, BorderLayout.EAST);

        // Event Listeners
        btnAdd.addActionListener(e -> tambahBarang());
        btnLoad.addActionListener(e -> loadBarang());
        btnUpdate.addActionListener(e -> updateBarang());
        btnDelete.addActionListener(e -> hapusBarang());
        btnClear.addActionListener(e -> clearFields());

        // Add Mouse Listener for Row Selection
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Isi form dengan data dari baris yang dipilih
                    txtNama.setText((String) tableModel.getValueAt(selectedRow, 1));  // Nama Barang
                    txtKategori.setText((String) tableModel.getValueAt(selectedRow, 2));  // Kategori
                    txtStock.setText(String.valueOf(tableModel.getValueAt(selectedRow, 3)));  // Stock Barang
                }
            }
        });

        // Load Data on Startup
        loadBarang();
    }

    private JTextField createTextField(String placeholder) {
        JTextField textField = new JTextField();
        textField.setBorder(BorderFactory.createTitledBorder(placeholder));
        return textField;
    }

//    private void setupDatabase() {
//        try {
//            connection = DriverManager.getConnection("jdbc:sqlite:warehouse.db");
//            Statement stmt = connection.createStatement();
//            String sql = "CREATE TABLE IF NOT EXISTS barang (" +
//                    "id_barang INTEGER PRIMARY KEY AUTOINCREMENT," +
//                    "nama_barang TEXT," +
//                    "kategori TEXT," +
//                    "stock_barang INTEGER" +
//                    ")";
//            stmt.execute(sql);
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
//        }
//    }

    private void tambahBarang() {
        try {
            String nama = txtNama.getText();
            String kategori = txtKategori.getText();
            int stock = Integer.parseInt(txtStock.getText());

            String sql = "INSERT INTO barang (nama_barang, kategori, stock_barang) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, nama);
            pstmt.setString(2, kategori);
            pstmt.setInt(3, stock);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Barang berhasil ditambahkan!");
            loadBarang();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void loadBarang() {
        try {
            tableModel.setRowCount(0);
            String sql = "SELECT * FROM barang";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id_barang"),
                        rs.getString("nama_barang"),
                        rs.getString("kategori"),
                        rs.getInt("stock_barang")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Load Error: " + e.getMessage());
        }
    }

    private void updateBarang() {
        try {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Pilih baris yang akan diupdate.");
                return;
            }

            String nama = txtNama.getText();
            String kategori = txtKategori.getText();
            int stock = Integer.parseInt(txtStock.getText());

            // Ambil ID dari baris yang dipilih
            int id = (int) tableModel.getValueAt(selectedRow, 0); // ID ada di kolom pertama

            String sql = "UPDATE barang SET nama_barang = ?, kategori = ?, stock_barang = ? WHERE id_barang = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, nama);
            pstmt.setString(2, kategori);
            pstmt.setInt(3, stock);
            pstmt.setInt(4, id);  // Update berdasarkan ID
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Barang berhasil diupdate!");
            loadBarang();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Update Error: " + e.getMessage());
        }
    }

    private void hapusBarang() {
        try {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Pilih baris yang akan dihapus.");
                return;
            }

            // Ambil ID dari baris yang dipilih
            int id = (int) tableModel.getValueAt(selectedRow, 0); // ID ada di kolom pertama

            String sql = "DELETE FROM barang WHERE id_barang = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, id);  // Hapus berdasarkan ID
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Barang berhasil dihapus!");
            loadBarang();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Delete Error: " + e.getMessage());
        }
    }

    private void clearFields() {
        txtNama.setText("");
        txtKategori.setText("");
        txtStock.setText("");
        table.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManajemenBarang().setVisible(true));
    }
}