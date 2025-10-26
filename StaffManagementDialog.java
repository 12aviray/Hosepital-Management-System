package com.hospital.management;

import javax.swing.*;
import java.awt.*;

public class StaffManagementDialog extends JDialog {
    private HospitalData data = HospitalData.getInstance();

    public StaffManagementDialog(JFrame owner) {
        super(owner, "Add New Staff Member", true);
        setSize(500, 600);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;


        JTextField nameField = new JTextField(20);
        JTextField ageField = new JTextField(5);
        JComboBox<String> genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        JTextField contactField = new JTextField(20);
        JTextField addressField = new JTextField(20);


        JComboBox<String> roleCombo = new JComboBox<>(new String[]{
                "Nurse", "Lab Technician", "Ward Boy", "Billing Officer", "Receptionist",
                "Security Guard", "Pharmacist", "Ambulance Driver", "OT Technician"
        });

        JTextField departmentField = new JTextField(20);
        JComboBox<String> shiftCombo = new JComboBox<>(new String[]{"Morning", "Evening", "Night"});


        int y = 0;
        gbc.gridy = y; gbc.gridx = 0; formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; formPanel.add(nameField, gbc);
        gbc.gridy = ++y; gbc.gridx = 0; formPanel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1; formPanel.add(ageField, gbc);
        gbc.gridy = ++y; gbc.gridx = 0; formPanel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1; formPanel.add(genderCombo, gbc);
        gbc.gridy = ++y; gbc.gridx = 0; formPanel.add(new JLabel("Contact Number:"), gbc);
        gbc.gridx = 1; formPanel.add(contactField, gbc);
        gbc.gridy = ++y; gbc.gridx = 0; formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1; formPanel.add(addressField, gbc);
        gbc.gridy = ++y; gbc.gridx = 0; formPanel.add(new JLabel("Role / Position:"), gbc);
        gbc.gridx = 1; formPanel.add(roleCombo, gbc);
        gbc.gridy = ++y; gbc.gridx = 0; formPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1; formPanel.add(departmentField, gbc);
        gbc.gridy = ++y; gbc.gridx = 0; formPanel.add(new JLabel("Duty Shift:"), gbc);
        gbc.gridx = 1; formPanel.add(shiftCombo, gbc);

        JButton saveButton = new JButton("Save Staff Information");
        add(new JScrollPane(formPanel), BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> {
            try {
                if (nameField.getText().isEmpty() || ageField.getText().isEmpty() || contactField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in at least Name, Age, and Contact.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Staff newStaff = new Staff(
                        data.getNextStaffId(), nameField.getText(), Integer.parseInt(ageField.getText()),
                        (String) genderCombo.getSelectedItem(), contactField.getText(), addressField.getText(),
                        (String) roleCombo.getSelectedItem(), departmentField.getText(), (String) shiftCombo.getSelectedItem()
                );
                data.addStaff(newStaff);
                JOptionPane.showMessageDialog(this, "Staff added successfully! ID: " + newStaff.getStaffId(), "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for Age.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Failed to Add Staff", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}