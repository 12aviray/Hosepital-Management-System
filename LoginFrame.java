package com.hospital.management;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("IIT HOSPITAL - Login");

        setSize(550, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        setExtendedState(JFrame.MAXIMIZED_BOTH);


        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));


        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 20, 20));

        JLabel titleLabel = new JLabel("IIT HOSPITAL", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        headerPanel.add(titleLabel, BorderLayout.NORTH);

        try {
            ImageIcon logo = new ImageIcon(new ImageIcon("hospital_logo.png").getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH));
            headerPanel.add(new JLabel(logo, SwingConstants.CENTER), BorderLayout.CENTER);
        } catch (Exception e) {
            headerPanel.add(new JLabel("Logo not found...", SwingConstants.CENTER), BorderLayout.CENTER);
        }


        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setOpaque(false);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 0; loginPanel.add(userLabel, gbc);
        JTextField userText = new JTextField(20);
        userText.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 1; loginPanel.add(userText, gbc);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 1; loginPanel.add(passLabel, gbc);
        JPasswordField passText = new JPasswordField(20);
        passText.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 1; loginPanel.add(passText, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginButton.setBackground(new Color(52, 152, 219));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        loginPanel.add(loginButton, gbc);

        add(headerPanel, BorderLayout.NORTH);
        add(loginPanel, BorderLayout.CENTER);

        loginButton.addActionListener(e -> {
            if (HospitalData.getInstance().validateUser(userText.getText(), new String(passText.getPassword()))) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                dispose();
                new MainDashboard().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> HospitalData.getInstance().saveData()));
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}