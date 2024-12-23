package org.example;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    public Main() {
        setTitle("Aplikasi Manajemen Gudang");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Pusatkan jendela
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(58, 134, 255));
        JLabel headerLabel = new JLabel("Aplikasi Manajemen Gudang");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Main Menu Buttons
        JPanel menuPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        menuPanel.setBackground(Color.WHITE);

        JButton btnBarang = createMenuButton("Manajemen Barang", "icons/items.png");
        JButton btnTransaksi = createMenuButton("Transaksi Barang", "icons/transaction.png");

        menuPanel.add(btnBarang);
        menuPanel.add(btnTransaksi);

        add(menuPanel, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(230, 230, 230));
        JLabel footerLabel = new JLabel("© 2024 Aplikasi Manajemen Gudang");
        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);

        // Action Listeners
        btnBarang.addActionListener(e -> new ManajemenBarang().setVisible(true));
        btnTransaksi.addActionListener(e -> new ManajemenBarangTransaksi().setVisible(true));
    }


    private JButton createMenuButton(String text, String iconPath) {
        JButton button = new JButton(text, new ImageIcon(iconPath));
        button.setFocusPainted(false);
        button.setBackground(new Color(240, 240, 240));
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        return button;
    }

    public static void main(String[] args) {
        DatabaseSetUp.initializeDatabase();
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));

    }

}