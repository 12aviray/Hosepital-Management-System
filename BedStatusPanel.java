package com.hospital.management;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Set;
import java.util.stream.Collectors;

public class BedStatusPanel extends JPanel {
    private CardLayout bedCardLayout;
    private JPanel bedMainPanel;
    private HospitalData data = HospitalData.getInstance();
    private DefaultTableModel bedTableModel;
    private JTable bedTable;
    private JLabel bedListTitle;

    public BedStatusPanel() {
        bedCardLayout = new CardLayout();
        bedMainPanel = new JPanel(bedCardLayout);


        JPanel wardSelectionPanel = createWardSelectionPanel();


        JPanel bedListPanel = createBedListPanel();

        bedMainPanel.add(wardSelectionPanel, "WARD_SELECTION");
        bedMainPanel.add(bedListPanel, "BED_LIST");

        setLayout(new BorderLayout());
        add(bedMainPanel, BorderLayout.CENTER);

        bedCardLayout.show(bedMainPanel, "WARD_SELECTION");
    }

    private JPanel createWardSelectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Select a Ward"));
        panel.setBackground(Color.WHITE);


        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Set<String> wards = data.bedsByWard.keySet();

        int y = 0;
        for (String ward : wards) {
            gbc.gridy = y++;
            JButton wardButton = createStyledButton(ward, new Color(99, 110, 114)); // Gray color
            wardButton.addActionListener(e -> {
                loadBedsByWard(ward);
                bedCardLayout.show(bedMainPanel, "BED_LIST");
            });
            centerPanel.add(wardButton, gbc);
        }

        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBedListPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("⬅ Back to Ward Selection");
        backButton.addActionListener(e -> bedCardLayout.show(bedMainPanel, "WARD_SELECTION"));
        topPanel.add(backButton, BorderLayout.WEST);

        bedListTitle = new JLabel("", SwingConstants.CENTER);
        bedListTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        topPanel.add(bedListTitle, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);

        String[] cols = {"Bed ID", "Room No", "Status"};
        bedTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bedTable = new JTable(bedTableModel);
        bedTable.setRowHeight(30);
        bedTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        bedTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        panel.add(new JScrollPane(bedTable), BorderLayout.CENTER);
        return panel;
    }

    private void loadBedsByWard(String ward) {
        bedListTitle.setText("Bed Status in: " + ward);
        if (bedTableModel == null) return;
        bedTableModel.setRowCount(0);

        if (data.bedsByWard.containsKey(ward)) {
            data.bedsByWard.get(ward).forEach(bed -> {
                bedTableModel.addRow(new Object[]{
                        bed.bedKey,
                        bed.roomNo,
                        bed.isVacant ? "Vacant" : "Occupied"
                });
            });
        }
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 24));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(450, 80));
        return button;
    }
}