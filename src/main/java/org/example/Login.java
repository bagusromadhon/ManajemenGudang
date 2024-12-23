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

    }

    private void handleLogin() {

    }

    private void saveSession(String username) {

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}