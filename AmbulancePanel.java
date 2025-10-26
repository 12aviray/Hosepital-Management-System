package com.hospital.management;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class AmbulancePanel extends JPanel {
    private DefaultTableModel tableModel;
    private HospitalData data = HospitalData.getInstance();

    public AmbulancePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Ambulance Fleet Status"));

        // Table
        String[] cols = {"Ambulance Number", "Status"};
        tableModel = new DefaultTableModel(cols, 0);
        JTable ambulanceTable = new JTable(tableModel);
        ambulanceTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        ambulanceTable.setRowHeight(30);

        loadAmbulanceData();

        add(new JScrollPane(ambulanceTable), BorderLayout.CENTER);
    }

    private void loadAmbulanceData() {
        tableModel.setRowCount(0); // Clear existing data

        // Ensure ambulances list is not null
        if (data.ambulances == null) {
            data.ambulances = new ArrayList<>();
        }

        data.ambulances.forEach(ambulance -> {
            tableModel.addRow(new Object[]{
                    ambulance.number,
                    ambulance.status
            });
        });
    }
}