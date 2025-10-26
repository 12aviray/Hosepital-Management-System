package com.hospital.management;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class AuthorityDialog extends JDialog {
    private HospitalData data = HospitalData.getInstance();
    private Authority authorityToUpdate;
    private JTextField nameField, ageField, contactField, addressField, emailField;
    private JComboBox<String> genderCombo, designationCombo, departmentCombo, statusCombo;
    private boolean isUpdateMode = false;

    public AuthorityDialog(JFrame owner, Authority authority) {
        super(owner, "Authority Member Information", true);
        this.authorityToUpdate = authority;
        this.isUpdateMode = (authority != null);
        setTitle(isUpdateMode ? "Update Authority Profile" : "Add New Authority");

        setSize(600, 550);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameField = new JTextField(20);
        ageField = new JTextField(5);
        genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        contactField = new JTextField(20);
        addressField = new JTextField(20);
        emailField = new JTextField(20);

        String[] designations = {"Hospital Director", "Chairman", "Hospital Administrator", "HR Manager", "Finance Manager", "IT Manager", "Chief Medical Officer (CMO)", "Head of Department (HoD)", "Nursing Superintendent", "Security Head", "Facility Manager"};
        String[] departments = {"Administration", "Board of Directors", "Human Resources", "Finance & Accounts", "IT", "Medical Administration", "Cardiology", "Nursing", "Maintenance & Facility", "Security"};

        designationCombo = new JComboBox<>(designations);
        departmentCombo = new JComboBox<>(departments);
        statusCombo = new JComboBox<>(new String[]{"Active", "Inactive"});

        int y = 0;
        addRow(formPanel, gbc, y++, new JLabel("Full Name:"), nameField);
        addRow(formPanel, gbc, y++, new JLabel("Age:"), ageField);
        addRow(formPanel, gbc, y++, new JLabel("Gender:"), genderCombo);
        addRow(formPanel, gbc, y++, new JLabel("Designation:"), designationCombo);
        addRow(formPanel, gbc, y++, new JLabel("Department:"), departmentCombo);
        addRow(formPanel, gbc, y++, new JLabel("Contact Number:"), contactField);
        addRow(formPanel, gbc, y++, new JLabel("Email:"), emailField);
        addRow(formPanel, gbc, y++, new JLabel("Address:"), addressField);
        addRow(formPanel, gbc, y++, new JLabel("Status:"), statusCombo);

        if (isUpdateMode) {
            populateFields();
        }

        JButton saveButton = new JButton(isUpdateMode ? "Update Information" : "Save Information");
        saveButton.addActionListener(e -> saveOrUpdate());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(saveButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void populateFields() {
        nameField.setText(authorityToUpdate.getName());
        nameField.setEditable(false); // Name and Age are usually not updatable
        ageField.setText(String.valueOf(authorityToUpdate.age));
        ageField.setEditable(false);
        genderCombo.setSelectedItem(authorityToUpdate.gender);
        designationCombo.setSelectedItem(authorityToUpdate.getDesignation());
        departmentCombo.setSelectedItem(authorityToUpdate.getDepartment());
        contactField.setText(authorityToUpdate.getContactNumber());
        addressField.setText(authorityToUpdate.address);
        emailField.setText(authorityToUpdate.getEmail());
        statusCombo.setSelectedItem(authorityToUpdate.getStatus());
    }

    private void saveOrUpdate() {
        try {
            if (isUpdateMode) {
                // Update existing authority
                authorityToUpdate.setDesignation((String) designationCombo.getSelectedItem());
                authorityToUpdate.setDepartment((String) departmentCombo.getSelectedItem());
                authorityToUpdate.setContactNumber(contactField.getText());
                authorityToUpdate.setAddress(addressField.getText());
                authorityToUpdate.setEmail(emailField.getText());
                authorityToUpdate.setStatus((String) statusCombo.getSelectedItem());
                JOptionPane.showMessageDialog(this, "Information updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Add new authority
                Authority newAuthority = new Authority(
                        data.getNextAuthorityId(), nameField.getText(), Integer.parseInt(ageField.getText()),
                        (String) genderCombo.getSelectedItem(), contactField.getText(), addressField.getText(),
                        (String) designationCombo.getSelectedItem(), (String) departmentCombo.getSelectedItem(),
                        emailField.getText(), new Date(), (String) statusCombo.getSelectedItem()
                );
                data.addAuthority(newAuthority);
                JOptionPane.showMessageDialog(this, "New authority member added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid age.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int y, JLabel label, JComponent component) {
        gbc.gridy = y;
        gbc.gridx = 0; panel.add(label, gbc);
        gbc.gridx = 1; panel.add(component, gbc);
    }
}