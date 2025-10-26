package com.hospital.management;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class DoctorPanel extends JPanel {
    private CardLayout doctorCardLayout;
    private JPanel doctorMainPanel;
    private HospitalData data = HospitalData.getInstance();

    private DefaultTableModel doctorTableModel;
    private JTable doctorTable;
    private JLabel doctorListTitle;

    public DoctorPanel() {
        doctorCardLayout = new CardLayout();
        doctorMainPanel = new JPanel(doctorCardLayout);

        doctorMainPanel.add(createDoctorHomePanel(), "HOME");
        doctorMainPanel.add(createDepartmentSelectionPanel(), "DEPARTMENT_SELECTION");
        doctorMainPanel.add(createDoctorListPanel(), "DOCTOR_LIST");

        setLayout(new BorderLayout());
        add(doctorMainPanel, BorderLayout.CENTER);

        doctorCardLayout.show(doctorMainPanel, "HOME");
    }

    public void switchToCard(String cardName) {
        if ("DEPARTMENT_SELECTION".equals(cardName)) {
            refreshDepartmentPanel();
        }
        doctorCardLayout.show(doctorMainPanel, cardName);
    }

    public void showNewDoctorDialog() {
        NewDoctorDialog dialog = new NewDoctorDialog((JFrame) SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
        switchToCard("DEPARTMENT_SELECTION");
    }

    private void refreshDepartmentPanel() {
        Component[] components = doctorMainPanel.getComponents();
        for (Component component : components) {
            if ("DEPARTMENT_SELECTION_PANEL".equals(component.getName())) {
                doctorMainPanel.remove(component);
                break;
            }
        }
        doctorMainPanel.add(createDepartmentSelectionPanel(), "DEPARTMENT_SELECTION");
    }

    private JPanel createDoctorHomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Doctor Management Menu");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        panel.add(title, gbc);

        gbc.gridy++;
        JButton viewButton = createStyledButton("View Doctors by Department", new Color(52, 152, 219), 24);
        viewButton.addActionListener(e -> switchToCard("DEPARTMENT_SELECTION"));
        panel.add(viewButton, gbc);

        gbc.gridy++;
        JButton addButton = createStyledButton("Add New Doctor", new Color(46, 204, 113), 24);
        addButton.addActionListener(e -> showNewDoctorDialog());
        panel.add(addButton, gbc);

        return panel;
    }

    private JPanel createDepartmentSelectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setName("DEPARTMENT_SELECTION_PANEL");
        panel.setBorder(BorderFactory.createTitledBorder("Select a Department"));

        JButton backButton = new JButton("⬅ Back to Doctor Menu");
        backButton.addActionListener(e -> doctorCardLayout.show(doctorMainPanel, "HOME"));
        panel.add(backButton, BorderLayout.NORTH);

        JPanel departmentGrid = new JPanel(new GridLayout(0, 4, 8, 8));
        departmentGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Set<String> departments = data.doctors.values().stream()
                .map(Doctor::getDepartment)
                .sorted()
                .collect(Collectors.toCollection(LinkedHashSet::new));

        for (String dept : departments) {
            JButton deptButton = createStyledButton(dept, new Color(99, 110, 114), 14);
            deptButton.addActionListener(e -> {
                loadDoctorsByDepartment(dept);
                doctorCardLayout.show(doctorMainPanel, "DOCTOR_LIST");
            });
            departmentGrid.add(deptButton);
        }


        JPanel gridContainerPanel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(departmentGrid);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        gridContainerPanel.add(scrollPane, BorderLayout.NORTH);


        panel.add(gridContainerPanel, BorderLayout.CENTER);


        return panel;
    }

    private JPanel createDoctorListPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("⬅ Back to Department Selection");
        backButton.addActionListener(e -> doctorCardLayout.show(doctorMainPanel, "DEPARTMENT_SELECTION"));
        topPanel.add(backButton, BorderLayout.WEST);

        doctorListTitle = new JLabel("", SwingConstants.CENTER);
        doctorListTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        topPanel.add(doctorListTitle, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);

        String[] cols = {"ID", "Name", "Designation", "Qualification", "Contact", "Duty Hours"};
        doctorTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        doctorTable = new JTable(doctorTableModel);
        doctorTable.setRowHeight(30);
        doctorTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        panel.add(new JScrollPane(doctorTable), BorderLayout.CENTER);

        doctorTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = doctorTable.getSelectedRow();
                    if (row >= 0) {
                        String doctorId = (String) doctorTable.getValueAt(row, 0);
                        showDoctorDetails(doctorId);
                    }
                }
            }
        });

        return panel;
    }

    private void loadDoctorsByDepartment(String department) {
        doctorListTitle.setText("Doctors in: " + department);
        if (doctorTableModel == null) return;
        doctorTableModel.setRowCount(0);
        data.doctors.values().stream()
                .filter(d -> d.getDepartment().equals(department))
                .forEach(d -> doctorTableModel.addRow(new Object[]{
                        d.getDoctorId(), d.getName(), d.getDesignation(),
                        d.getQualification(), d.getContactNumber(),
                        d.getDutyHours()
                }));
    }

    private void showDoctorDetails(String doctorId) {
        Doctor d = data.doctors.get(doctorId);
        if (d == null) return;

        JTextArea textArea = new JTextArea(20, 50);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        StringBuilder details = new StringBuilder();
        details.append("===== DOCTOR DETAILS (ID: ").append(d.getDoctorId()).append(") =====\n\n");
        details.append(String.format("%-20s: %s\n", "Name", d.getName()));
        details.append(String.format("%-20s: %d\n", "Age", d.getAge()));
        details.append(String.format("%-20s: %s\n", "Gender", d.getGender()));
        details.append(String.format("%-20s: %s\n", "Contact", d.getContactNumber()));
        details.append(String.format("%-20s: %s\n", "Address", d.getAddress()));
        details.append("\n===== PROFESSIONAL DETAILS =====\n\n");
        details.append(String.format("%-20s: %s\n", "Department", d.getDepartment()));
        details.append(String.format("%-20s: %s\n", "Specialization", d.getSpecialization()));
        details.append(String.format("%-20s: %s\n", "Qualification", d.getQualification()));
        details.append(String.format("%-20s: %s\n", "Designation", d.getDesignation()));
        details.append(String.format("%-20s: %s\n", "BMDC License", d.getBmdcLicense()));
        details.append(String.format("%-20s: %s\n", "Duty Hours", d.getDutyHours()));
        details.append(String.format("%-20s: %s\n", "Joining Date", new SimpleDateFormat("dd-MMM-yyyy").format(d.getJoiningDate())));
        details.append(String.format("%-20s: %s\n", "Status", d.getStatus()));

        textArea.setText(details.toString());
        textArea.setCaretPosition(0);
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Doctor Full Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private JButton createStyledButton(String text, Color color, int fontSize) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (fontSize > 20) {
            button.setPreferredSize(new Dimension(450, 80));
        } else {
            button.setBorder(BorderFactory.createEmptyBorder(15, 8, 15, 8));
        }
        return button;
    }
}