package com.hospital.management;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthorityPanel extends JPanel {
    private CardLayout authorityCardLayout;
    private JPanel authorityMainPanel;
    private HospitalData data = HospitalData.getInstance();
    private DefaultTableModel authorityTableModel;
    private JTable authorityTable;
    private JLabel authorityListTitle;

    public AuthorityPanel() {
        authorityCardLayout = new CardLayout();
        authorityMainPanel = new JPanel(authorityCardLayout);

        authorityMainPanel.add(createAuthorityHomePanel(), "HOME");
        authorityMainPanel.add(createDesignationSelectionPanel(), "DESIGNATION_SELECTION");
        authorityMainPanel.add(createAuthorityListPanel(), "LIST_VIEW");
        authorityMainPanel.add(createPermissionsPanel(), "PERMISSIONS");
        authorityMainPanel.add(createReportsPanel(), "REPORTS");

        setLayout(new BorderLayout());
        add(authorityMainPanel, BorderLayout.CENTER);

        authorityCardLayout.show(authorityMainPanel, "HOME");
    }

    private JPanel createAuthorityHomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0;
        panel.add(createStyledButton("View Authority List", new Color(52, 152, 219)), gbc);
        gbc.gridy++;
        panel.add(createStyledButton("Add New Authority", new Color(26, 188, 156)), gbc);
        gbc.gridy++;
        panel.add(createStyledButton("Access & Permissions", new Color(142, 68, 173)), gbc);
        gbc.gridy++;
        panel.add(createStyledButton("Reports & Monitoring", new Color(243, 156, 18)), gbc);


        ((JButton) panel.getComponent(0)).addActionListener(e -> authorityCardLayout.show(authorityMainPanel, "DESIGNATION_SELECTION"));
        ((JButton) panel.getComponent(1)).addActionListener(e -> openAuthorityDialog(null));
        ((JButton) panel.getComponent(2)).addActionListener(e -> authorityCardLayout.show(authorityMainPanel, "PERMISSIONS"));
        ((JButton) panel.getComponent(3)).addActionListener(e -> authorityCardLayout.show(authorityMainPanel, "REPORTS"));

        return panel;
    }

    private JPanel createPermissionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Manage Access & Permissions"));

        JButton backButton = new JButton("⬅ Back to Authority Menu");
        backButton.addActionListener(e -> authorityCardLayout.show(authorityMainPanel, "HOME"));
        panel.add(backButton, BorderLayout.NORTH);

        JPanel mainContent = new JPanel(new GridLayout(1, 2, 10, 10));
        mainContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel rolePanel = new JPanel(new BorderLayout());
        rolePanel.setBorder(BorderFactory.createTitledBorder("Select Role"));
        String[] roles = {"Doctor", "Nurse", "Receptionist", "Billing Officer"};
        JList<String> roleList = new JList<>(roles);
        roleList.setFont(new Font("Segoe UI", Font.BOLD, 16));
        rolePanel.add(new JScrollPane(roleList), BorderLayout.CENTER);

        JPanel permissionsPanel = new JPanel();
        permissionsPanel.setLayout(new BoxLayout(permissionsPanel, BoxLayout.Y_AXIS));
        permissionsPanel.setBorder(BorderFactory.createTitledBorder("Permissions"));

        mainContent.add(rolePanel);
        mainContent.add(permissionsPanel);

        JCheckBox viewPatientRecords = new JCheckBox("View Patient Records");
        JCheckBox editPatientRecords = new JCheckBox("Edit Patient Records");
        JCheckBox dischargePatients = new JCheckBox("Discharge Patients");
        JCheckBox viewBillingInfo = new JCheckBox("View Billing Info");
        JCheckBox editBillingInfo = new JCheckBox("Edit Billing Info (e.g., add charges)");
        JCheckBox manageStaffSchedule = new JCheckBox("Manage Staff Schedule");

        permissionsPanel.add(viewPatientRecords);
        permissionsPanel.add(editPatientRecords);
        permissionsPanel.add(dischargePatients);
        permissionsPanel.add(viewBillingInfo);
        permissionsPanel.add(editBillingInfo);
        permissionsPanel.add(manageStaffSchedule);

        roleList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedRole = roleList.getSelectedValue();
                if (selectedRole != null) {
                    switch (selectedRole) {
                        case "Doctor":
                            viewPatientRecords.setSelected(true); editPatientRecords.setSelected(true);
                            dischargePatients.setSelected(true); viewBillingInfo.setSelected(true);
                            editBillingInfo.setSelected(false); manageStaffSchedule.setSelected(false);
                            break;
                        case "Nurse":
                            viewPatientRecords.setSelected(true); editPatientRecords.setSelected(true);
                            dischargePatients.setSelected(false); viewBillingInfo.setSelected(false);
                            editBillingInfo.setSelected(false); manageStaffSchedule.setSelected(false);
                            break;
                        case "Receptionist":
                            viewPatientRecords.setSelected(true); editPatientRecords.setSelected(false);
                            dischargePatients.setSelected(false); viewBillingInfo.setSelected(true);
                            editBillingInfo.setSelected(false); manageStaffSchedule.setSelected(true);
                            break;
                        case "Billing Officer":
                            viewPatientRecords.setSelected(true); editPatientRecords.setSelected(false);
                            dischargePatients.setSelected(false); viewBillingInfo.setSelected(true);
                            editBillingInfo.setSelected(true); manageStaffSchedule.setSelected(false);
                            break;
                    }
                }
            }
        });

        JButton savePermissionsButton = new JButton("Save Permissions");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(savePermissionsButton);

        panel.add(mainContent, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Reports & Monitoring Dashboard"));

        JButton backButton = new JButton("⬅ Back to Authority Menu");
        backButton.addActionListener(e -> authorityCardLayout.show(authorityMainPanel, "HOME"));
        panel.add(backButton, BorderLayout.NORTH);

        JPanel reportButtonsContainer = new JPanel(new BorderLayout(0, 10));
        reportButtonsContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel selectReportLabel = new JLabel("Select a Report to Generate");
        selectReportLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        reportButtonsContainer.add(selectReportLabel, BorderLayout.NORTH);

        JPanel reportButtonsGrid = new JPanel(new GridLayout(0, 1, 10, 10));

        JTextArea reportDisplayArea = new JTextArea("Select a report from the left to view details here.");
        reportDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        reportDisplayArea.setEditable(false);
        reportDisplayArea.setMargin(new Insets(10, 10, 10, 10));
        reportDisplayArea.setBackground(new Color(248, 249, 250));

        JButton dailyPatientSummary = createReportButton("📊 Daily Patient Summary");
        JButton monthlyRevenue = createReportButton("💰 Monthly Revenue Report");
        JButton departmentPerformance = createReportButton("🏥 Department Performance");
        JButton staffSummary = createReportButton("👥 Staff Summary Report");
        JButton ambulanceStatus = createReportButton("🚑 Ambulance Status Report");

        reportButtonsGrid.add(dailyPatientSummary);
        reportButtonsGrid.add(monthlyRevenue);
        reportButtonsGrid.add(departmentPerformance);
        reportButtonsGrid.add(staffSummary);
        reportButtonsGrid.add(ambulanceStatus);
        reportButtonsContainer.add(reportButtonsGrid, BorderLayout.CENTER);

        dailyPatientSummary.addActionListener(e -> {
            long admittedCount = data.patients.values().stream().filter(p -> !p.isDischarged()).count();
            long dischargedCount = data.patients.values().stream().filter(Patient::isDischarged).count();
            reportDisplayArea.setText(
                    "===== DAILY PATIENT SUMMARY (" + new SimpleDateFormat("dd-MMM-yyyy").format(new Date()) + ") =====\n\n" +
                            String.format("%-30s: %d\n", "Currently Admitted Patients", admittedCount) +
                            String.format("%-30s: %d\n", "Total Discharged Patients", dischargedCount) +
                            String.format("%-30s: %d\n", "Total Patients in System", data.patients.size())
            );
            reportDisplayArea.setCaretPosition(0);
        });

        monthlyRevenue.addActionListener(e -> {
            double totalAdvance = data.patients.values().stream().mapToDouble(Patient::getAdvancePayment).sum();
            reportDisplayArea.setText(
                    "===== MONTHLY REVENUE REPORT (SAMPLE) =====\n\n" +
                            "This is a simplified report based on advance payments only.\n\n" +
                            String.format("%-30s: %.2f BDT\n", "Total Advance Collected", totalAdvance) +
                            String.format("%-30s: %.2f BDT\n", "Estimated Other Charges", totalAdvance * 2.5) +
                            "--------------------------------------------------\n" +
                            String.format("%-30s: %.2f BDT\n", "Estimated Total Revenue", totalAdvance * 3.5)
            );
            reportDisplayArea.setCaretPosition(0);
        });

        departmentPerformance.addActionListener(e -> {
            Map<String, Long> patientCountByDept = data.patients.values().stream()
                    .filter(p -> !p.isDischarged())
                    .collect(Collectors.groupingBy(Patient::getDepartment, Collectors.counting()));

            StringBuilder report = new StringBuilder("===== DEPARTMENT PERFORMANCE (Current Patients) =====\n\n");
            report.append(String.format("%-30s | %s\n", "DEPARTMENT", "PATIENT COUNT"));
            report.append("-------------------------------------------------\n");
            patientCountByDept.forEach((dept, count) ->
                    report.append(String.format("%-30s | %d\n", dept, count))
            );
            reportDisplayArea.setText(report.toString());
            reportDisplayArea.setCaretPosition(0);
        });

        staffSummary.addActionListener(e -> {
            Map<String, Long> staffCountByRole = data.staff.values().stream()
                    .collect(Collectors.groupingBy(Staff::getRole, Collectors.counting()));
            Map<String, Long> staffCountByShift = data.staff.values().stream()
                    .collect(Collectors.groupingBy(Staff::getDutyShift, Collectors.counting()));

            StringBuilder report = new StringBuilder("===== STAFF SUMMARY REPORT =====\n\n");
            report.append(String.format("Total Staff Members: %d\n\n", data.staff.size()));

            report.append("--- Staff by Role ---\n");
            report.append(String.format("%-20s | %s\n", "ROLE", "COUNT"));
            report.append("---------------------------------\n");
            staffCountByRole.forEach((role, count) ->
                    report.append(String.format("%-20s | %d\n", role, count))
            );

            report.append("\n--- Staff by Shift ---\n");
            report.append(String.format("%-20s | %s\n", "SHIFT", "COUNT"));
            report.append("---------------------------------\n");
            staffCountByShift.forEach((shift, count) ->
                    report.append(String.format("%-20s | %d\n", shift, count))
            );

            reportDisplayArea.setText(report.toString());
            reportDisplayArea.setCaretPosition(0);
        });

        ambulanceStatus.addActionListener(e -> {
            Map<String, Long> ambulanceCountByStatus = data.ambulances.stream()
                    .collect(Collectors.groupingBy(ambulance -> ambulance.status, Collectors.counting()));

            StringBuilder report = new StringBuilder("===== AMBULANCE FLEET STATUS =====\n\n");
            report.append(String.format("Total Ambulances: %d\n\n", data.ambulances.size()));
            report.append(String.format("%-20s | %s\n", "STATUS", "COUNT"));
            report.append("---------------------------------\n");
            ambulanceCountByStatus.forEach((status, count) ->
                    report.append(String.format("%-20s | %d\n", status, count))
            );

            reportDisplayArea.setText(report.toString());
            reportDisplayArea.setCaretPosition(0);
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, reportButtonsContainer, new JScrollPane(reportDisplayArea));
        splitPane.setDividerLocation(300);
        panel.add(splitPane, BorderLayout.CENTER);

        return panel;
    }

    private JButton createReportButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(10);
        button.setBackground(new Color(236, 240, 241));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        return button;
    }

    private JPanel createDesignationSelectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Select a Designation"));

        JButton backButton = new JButton("⬅ Back to Authority Menu");
        backButton.addActionListener(e -> authorityCardLayout.show(authorityMainPanel, "HOME"));
        panel.add(backButton, BorderLayout.NORTH);

        JPanel designationGrid = new JPanel(new GridLayout(0, 2, 15, 15));
        designationGrid.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        Set<String> designations = data.authorities.values().stream()
                .map(Authority::getDesignation)
                .collect(Collectors.toSet());

        for (String designation : designations) {
            JButton designationButton = createStyledButton(designation, new Color(99, 110, 114));
            designationButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
            designationButton.setPreferredSize(new Dimension(300, 70));
            designationButton.addActionListener(e -> {
                loadAuthorityByDesignation(designation);
                authorityCardLayout.show(authorityMainPanel, "LIST_VIEW");
            });
            designationGrid.add(designationButton);
        }

        JPanel gridContainerPanel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(designationGrid);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        gridContainerPanel.add(scrollPane, BorderLayout.NORTH);
        panel.add(gridContainerPanel, BorderLayout.CENTER);

        return panel;
    }
    private JPanel createAuthorityListPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("⬅ Back to Designation Selection");
        backButton.addActionListener(e -> authorityCardLayout.show(authorityMainPanel, "DESIGNATION_SELECTION"));
        topPanel.add(backButton, BorderLayout.WEST);

        authorityListTitle = new JLabel("", SwingConstants.CENTER);
        authorityListTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        topPanel.add(authorityListTitle, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);

        String[] cols = {"ID", "Name", "Department", "Contact", "Status"};
        authorityTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        authorityTable = new JTable(authorityTableModel);
        authorityTable.setRowHeight(30);

        panel.add(new JScrollPane(authorityTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton updateButton = new JButton("Update Selected Profile");
        bottomPanel.add(updateButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        updateButton.addActionListener(e -> {
            int selectedRow = authorityTable.getSelectedRow();
            if (selectedRow >= 0) {
                String authorityId = (String) authorityTable.getValueAt(selectedRow, 0);
                openAuthorityDialog(data.authorities.get(authorityId));
            } else {
                JOptionPane.showMessageDialog(this, "Please select a member to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        authorityTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = authorityTable.getSelectedRow();
                    if (row >= 0) {
                        String authorityId = (String) authorityTable.getValueAt(row, 0);
                        showAuthorityDetails(data.authorities.get(authorityId));
                    }
                }
            }
        });

        return panel;
    }

    private void loadAuthorityByDesignation(String designation) {
        authorityListTitle.setText("Members with Designation: " + designation);
        authorityTableModel.setRowCount(0);
        data.authorities.values().stream()
                .filter(a -> a.getDesignation().equals(designation))
                .forEach(a -> authorityTableModel.addRow(new Object[]{
                        a.getAuthorityId(), a.getName(),
                        a.getDepartment(), a.getContactNumber(), a.getStatus()
                }));
    }

    private void openAuthorityDialog(Authority authority) {
        AuthorityDialog dialog = new AuthorityDialog((JFrame) SwingUtilities.getWindowAncestor(this), authority);
        dialog.setVisible(true);
        if (authority != null) {
            loadAuthorityByDesignation(authority.getDesignation());
        }
    }

    private void showAuthorityDetails(Authority a) {
        JTextArea textArea = new JTextArea(15, 45);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        StringBuilder details = new StringBuilder();
        details.append("===== AUTHORITY PROFILE =====\n\n");
        details.append(String.format("%-20s: %s (ID: %s)\n", "Name", a.getName(), a.getAuthorityId()));
        details.append(String.format("%-20s: %s\n", "Designation", a.getDesignation()));
        details.append(String.format("%-20s: %s\n", "Department", a.getDepartment()));
        details.append(String.format("%-20s: %s\n", "Contact", a.getContactNumber()));
        details.append(String.format("%-20s: %s\n", "Email", a.getEmail()));
        details.append(String.format("%-20s: %s\n", "Address", a.address));
        details.append(String.format("%-20s: %s\n", "Joining Date", new SimpleDateFormat("dd-MMM-yyyy").format(a.getJoiningDate())));
        details.append(String.format("%-20s: %s\n", "Status", a.getStatus()));
        textArea.setText(details.toString());
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Authority Full Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 22));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(400, 75));
        return button;
    }
}