package com.hospital.management;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {

    public HomePanel(CardLayout cardLayout, JPanel mainPanel) {
        // Use GridBagLayout for precise control over component placement\\\\
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();


        JLabel titleLabel = new JLabel("IIT HOSPITAL DASHBOARD");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 34));
        titleLabel.setForeground(new Color(45, 52, 54));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 30, 0);
        add(titleLabel, gbc);


        JPanel leftColumn = new JPanel(new GridBagLayout());
        leftColumn.setBackground(Color.WHITE);
        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.insets = new Insets(10, 0, 10, 0);
        gbcLeft.gridx = 0;
        gbcLeft.fill = GridBagConstraints.HORIZONTAL;

        gbcLeft.gridy = 0;
        leftColumn.add(createModuleButton("Patient Management", new Color(26, 188, 156), cardLayout, mainPanel), gbcLeft);
        gbcLeft.gridy++;
        leftColumn.add(createModuleButton("Doctor Management", new Color(52, 152, 219), cardLayout, mainPanel), gbcLeft);
        gbcLeft.gridy++;
        leftColumn.add(createModuleButton("Staff Management", new Color(155, 89, 182), cardLayout, mainPanel), gbcLeft);


        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 20, 0, 20);
        gbc.anchor = GridBagConstraints.NORTH;
        add(leftColumn, gbc);


        JPanel rightColumn = new JPanel(new GridBagLayout());
        rightColumn.setBackground(Color.WHITE);
        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.insets = new Insets(10, 0, 10, 0);
        gbcRight.gridx = 0;
        gbcRight.fill = GridBagConstraints.HORIZONTAL;

        gbcRight.gridy = 0;
        rightColumn.add(createModuleButton("Bed Status", new Color(241, 196, 15), cardLayout, mainPanel), gbcRight);
        gbcRight.gridy++;
        rightColumn.add(createModuleButton("Ambulance Status", new Color(46, 204, 113), cardLayout, mainPanel), gbcRight);
        gbcRight.gridy++;
        rightColumn.add(createModuleButton("Reports", new Color(230, 126, 34), cardLayout, mainPanel), gbcRight);


        gbc.gridx = 1;
        gbc.gridy = 1;
        add(rightColumn, gbc);


        JPanel authorityPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        authorityPanel.setBackground(Color.WHITE);
        authorityPanel.add(createModuleButton("Authority", new Color(231, 76, 60), cardLayout, mainPanel));

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 10, 0);
        add(authorityPanel, gbc);
    }

    private JButton createModuleButton(String text, Color bgColor, CardLayout cardLayout, JPanel mainPanel) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        button.setPreferredSize(new Dimension(380, 65));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> cardLayout.show(mainPanel, text));
        return button;
    }
}