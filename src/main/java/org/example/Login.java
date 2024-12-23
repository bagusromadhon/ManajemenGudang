// Login.java
package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class Login extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public Login() {
        setTitle("Login - Aplikasi Manajemen Gudang");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame
        setLayout(new GridLayout(4, 1, 10, 10));

        // UI Components
        JLabel lblTitle = new JLabel("Login", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitle);

        JPanel usernamePanel = new JPanel(new BorderLayout());
        usernamePanel.add(new JLabel("Username: "), BorderLayout.WEST);
        txtUsername = new JTextField();
        usernamePanel.add(txtUsername, BorderLayout.CENTER);
        add(usernamePanel);

        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.add(new JLabel("Password: "), BorderLayout.WEST);
        txtPassword = new JPasswordField();
        passwordPanel.add(txtPassword, BorderLayout.CENTER);
        add(passwordPanel);

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(e -> handleLogin());
        add(btnLogin);

        // Establish Database Connection
        DatabaseSetUp.initializeDatabase();
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan Password tidak boleh kosong!");
            return;
        }

        try (Connection connection = DatabaseSetUp.getConnection("jdbc:sqlite:warehouse.db")) {
            // Validate credentials from SQLite
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login berhasil!");

                // Save session to a file
                saveSession(username);

                // Open Main Window
                new Main().setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Username atau Password salah!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void saveSession(String username) {
        File sessionFile = new File("session.txt");
        try (FileWriter writer = new FileWriter(sessionFile)) {
            writer.write(username);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan sesi: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}
