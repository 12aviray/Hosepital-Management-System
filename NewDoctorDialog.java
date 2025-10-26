package com.hospital.management;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

public class NewDoctorDialog extends JDialog {
    private HospitalData data = HospitalData.getInstance();

    public NewDoctorDialog(JFrame owner) {
        super(owner, "Add New Doctor Form", true);
        setSize(600, 620);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Fields
        JTextField nameField = new JTextField(20);
        nameField.setFont(fieldFont);
        JTextField ageField = new JTextField(5);
        ageField.setFont(fieldFont);
        JComboBox<String> genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderCombo.setFont(fieldFont);
        JTextField contactField = new JTextField(20);
        contactField.setFont(fieldFont);
        JTextField addressField = new JTextField(20);
        addressField.setFont(fieldFont);


        Set<String> existingDepts = data.doctors.values().stream().map(Doctor::getDepartment).collect(Collectors.toSet());
        JComboBox<String> departmentCombo = new JComboBox<>(new Vector<>(existingDepts));
        departmentCombo.setEditable(true);
        departmentCombo.setFont(fieldFont);

        String[] qualifications = {"MBBS", "MD", "MS", "FCPS", "PhD", "DCH", "Diploma"};
        String[] designations = {"Consultant", "Senior Consultant", "Junior Consultant", "Professor", "Associate Professor", "RMO"};
        JComboBox<String> qualCombo = new JComboBox<>(qualifications);
        qualCombo.setFont(fieldFont);
        JComboBox<String> desigCombo = new JComboBox<>(designations);
        desigCombo.setFont(fieldFont);
        JTextField specField = new JTextField(20);
        specField.setFont(fieldFont);
        JTextField licenseField = new JTextField(20);
        licenseField.setFont(fieldFont);
        JTextField dutyField = new JTextField(20);
        dutyField.setFont(fieldFont);
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Active", "On Leave", "Inactive"});
        statusCombo.setFont(fieldFont);

        // Layout
        int y = 0;
        addRow(formPanel, gbc, y++, new JLabel("Full Name:"), nameField, labelFont);
        addRow(formPanel, gbc, y++, new JLabel("Age:"), ageField, labelFont);
        addRow(formPanel, gbc, y++, new JLabel("Gender:"), genderCombo, labelFont);
        addRow(formPanel, gbc, y++, new JLabel("Contact Number:"), contactField, labelFont);
        addRow(formPanel, gbc, y++, new JLabel("Address:"), addressField, labelFont);
        addRow(formPanel, gbc, y++, new JLabel("Department:"), departmentCombo, labelFont);
        addRow(formPanel, gbc, y++, new JLabel("Specialization:"), specField, labelFont);
        addRow(formPanel, gbc, y++, new JLabel("Qualification:"), qualCombo, labelFont);
        addRow(formPanel, gbc, y++, new JLabel("Designation:"), desigCombo, labelFont);
        addRow(formPanel, gbc, y++, new JLabel("BMDC License:"), licenseField, labelFont);
        addRow(formPanel, gbc, y++, new JLabel("Duty Hours:"), dutyField, labelFont);
        addRow(formPanel, gbc, y++, new JLabel("Status:"), statusCombo, labelFont);

        JButton saveButton = new JButton("Save Doctor Information");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        saveButton.setBackground(new Color(46, 204, 113));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        buttonPanel.add(saveButton);

        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.NORTH);

        saveButton.addActionListener(e -> {
            try {
                if (nameField.getText().isEmpty() || ageField.getText().isEmpty() || contactField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill required fields (Name, Age, Contact).", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String department = (String) departmentCombo.getSelectedItem();
                if (department == null || department.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Department cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Doctor newDoctor = new Doctor(
                        data.getNextDoctorId(), nameField.getText(), Integer.parseInt(ageField.getText()),
                        (String) genderCombo.getSelectedItem(), contactField.getText(), addressField.getText(),
                        specField.getText(), (String) qualCombo.getSelectedItem(),
                        department.trim(), (String) desigCombo.getSelectedItem(),
                        licenseField.getText(), dutyField.getText(), new Date(), (String) statusCombo.getSelectedItem()
                );
                data.addDoctor(newDoctor);
                JOptionPane.showMessageDialog(this, "Doctor added successfully! ID: " + newDoctor.getDoctorId(), "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for Age.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int y, JLabel label, JComponent component, Font font) {
        label.setFont(font);
        gbc.gridy = y;
        gbc.gridx = 0;
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(component, gbc);
    }
}