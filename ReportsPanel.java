package com.hospital.management;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportsPanel extends JPanel {
    private HospitalData data = HospitalData.getInstance();
    private JLabel totalPatientsLabel, admittedLabel, dischargedLabel, bedOccupancyLabel;
    private JTextArea departmentStatsArea;

    public ReportsPanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createTitledBorder("Hospital Statistics Report"));

        JPanel statsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        totalPatientsLabel = createStatLabel();
        admittedLabel = createStatLabel();
        dischargedLabel = createStatLabel();
        bedOccupancyLabel = createStatLabel();

        statsPanel.add(new JLabel("Total Patients (All Time):"));
        statsPanel.add(totalPatientsLabel);
        statsPanel.add(new JLabel("Currently Admitted Patients:"));
        statsPanel.add(admittedLabel);
        statsPanel.add(new JLabel("Total Discharged Patients:"));
        statsPanel.add(dischargedLabel);
        statsPanel.add(new JLabel("Bed Occupancy Rate:"));
        statsPanel.add(bedOccupancyLabel);

        departmentStatsArea = new JTextArea(10, 30);
        departmentStatsArea.setFont(new Font("Monospaced", Font.BOLD, 16));
        departmentStatsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(departmentStatsArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Admitted Patients by Department"));

        JButton refreshButton = new JButton("Refresh Statistics");
        refreshButton.addActionListener(e -> updateReports());

        add(statsPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
        add(refreshButton, BorderLayout.SOUTH);

        updateReports();
    }

    private JLabel createStatLabel() {
        JLabel label = new JLabel();
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(new Color(0, 102, 204));
        return label;
    }

    private void updateReports() {
        long total = data.patients.size();
        long admitted = data.patients.values().stream().filter(p -> !p.isDischarged()).count();
        long discharged = total - admitted;

        totalPatientsLabel.setText(String.valueOf(total));
        admittedLabel.setText(String.valueOf(admitted));
        dischargedLabel.setText(String.valueOf(discharged));

        long totalBeds = data.bedsByWard.values().stream().mapToLong(java.util.List::size).sum();
        long occupiedBeds = totalBeds - data.bedsByWard.values().stream().flatMap(java.util.List::stream).filter(b -> b.isVacant).count();
        double occupancyRate = (totalBeds > 0) ? ((double) occupiedBeds / totalBeds) * 100 : 0;
        bedOccupancyLabel.setText(String.format("%d / %d (%.2f%%)", occupiedBeds, totalBeds, occupancyRate));


        Map<String, Long> departmentCounts = data.patients.values().stream()
                .filter(p -> !p.isDischarged())
                .collect(Collectors.groupingBy(Patient::getDepartment, Collectors.counting()));

        StringBuilder deptText = new StringBuilder("Department\t\t| Patients\n");
        deptText.append("------------------------------------------\n");
        departmentCounts.forEach((dept, count) ->
                deptText.append(String.format("%-25s| %d\n", dept, count))
        );
        departmentStatsArea.setText(deptText.toString());
    }
}