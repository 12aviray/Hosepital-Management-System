package com.hospital.management;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.Vector;
import java.util.stream.Collectors;


public class AdmitPatientDialog extends JDialog {
    private HospitalData data = HospitalData.getInstance();

    public AdmitPatientDialog(JFrame owner) {
        super(owner, "New Patient Admission Form", true);
        setSize(600, 700);
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
        JComboBox<String> bloodGroupCombo = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        JTextField addressField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JTextArea symptomsArea = new JTextArea(3, 20);
        JComboBox<String> departmentCombo = new JComboBox<>(new Vector<>(data.doctors.values().stream().map(Doctor::getDepartment).distinct().sorted().collect(Collectors.toList())));
        JComboBox<String> doctorCombo = new JComboBox<>();
        JComboBox<String> wardCombo = new JComboBox<>(new Vector<>(data.bedsByWard.keySet()));
        JComboBox<String> roomCombo = new JComboBox<>();
        JComboBox<String> bedCombo = new JComboBox<>();
        JTextField guardianNameField = new JTextField(20);
        JComboBox<String> paymentCombo = new JComboBox<>(new String[]{"Cash", "Card", "Mobile Banking", "Insurance"});
        JTextField advanceField = new JTextField(10);


        int y = 0;
        // Personal Details
        gbc.gridy = y; gbc.gridx = 0; gbc.gridwidth = 2; formPanel.add(new JLabel("--- Personal Details ---"), gbc); y++;
        gbc.gridwidth = 1;
        gbc.gridy = y; gbc.gridx = 0; formPanel.add(new JLabel("Name:"), gbc); gbc.gridx = 1; formPanel.add(nameField, gbc);
        gbc.gridy = ++y; gbc.gridx = 0; formPanel.add(new JLabel("Age:"), gbc); gbc.gridx = 1; formPanel.add(ageField, gbc);
        gbc.gridy = ++y; gbc.gridx = 0; formPanel.add(new JLabel("Gender:"), gbc); gbc.gridx = 1; formPanel.add(genderCombo, gbc);
        gbc.gridy = ++y; gbc.gridx = 0; formPanel.add(new JLabel("Blood Group:"), gbc); gbc.gridx = 1; formPanel.add(bloodGroupCombo, gbc);
        gbc.gridy = ++y; gbc.gridx = 0; formPanel.add(new JLabel("Address:"), gbc); gbc.gridx = 1; formPanel.add(addressField, gbc);
        gbc.gridy = ++y; gbc.gridx = 0; formPanel.add(new JLabel("Phone:"), gbc); gbc.gridx = 1; formPanel.add(phoneField, gbc);

        // Medical Details
        gbc.gridy = ++y; gbc.gridx = 0; gbc.gridwidth = 2; formPanel.add(new JLabel("--- Medical Details ---"), gbc); y++;
        gbc.gridwidth = 1;
        gbc.gridy = y; gbc.gridx = 0; formPanel.add(new JLabel("Symptoms:"), gbc); gbc.gridx = 1; formPanel.add(new JScrollPane(symptomsArea), gbc);
        gbc.gridy = ++y; gbc.gridx = 0; formPanel.add(new JLabel("Department:"), gbc); gbc.gridx = 1; formPanel.add(departmentCombo, gbc);
        gbc.gridy = ++y; gbc.gridx = 0; formPanel.add(new JLabel("Doctor:"), gbc); gbc.gridx = 1; formPanel.add(doctorCombo, gbc);
        gbc.gridy = ++y; gbc.gridx = 0; formPanel.add(new JLabel("Ward:"), gbc); gbc.gridx = 1; formPanel.add(wardCombo, gbc);
        gbc.gridy = ++y; gbc.gridx = 0; formPanel.add(new JLabel("Room:"), gbc); gbc.gridx = 1; formPanel.add(roomCombo, gbc);
        gbc.gridy = ++y; gbc.gridx = 0; formPanel.add(new JLabel("Bed No:"), gbc); gbc.gridx = 1; formPanel.add(bedCombo, gbc);

        // Guardian & Billing
        gbc.gridy = ++y; gbc.gridx = 0; gbc.gridwidth = 2; formPanel.add(new JLabel("--- Guardian & Billing ---"), gbc); y++;
        gbc.gridwidth = 1;
        gbc.gridy = y; gbc.gridx = 0; formPanel.add(new JLabel("Guardian Name:"), gbc); gbc.gridx = 1; formPanel.add(guardianNameField, gbc);
        gbc.gridy = ++y; gbc.gridx = 0; formPanel.add(new JLabel("Payment Method:"), gbc); gbc.gridx = 1; formPanel.add(paymentCombo, gbc);
        gbc.gridy = ++y; gbc.gridx = 0; formPanel.add(new JLabel("Advance:"), gbc); gbc.gridx = 1; formPanel.add(advanceField, gbc);

        departmentCombo.addActionListener(e -> updateDoctorCombo((String) departmentCombo.getSelectedItem(), doctorCombo));
        wardCombo.addActionListener(e -> updateRoomCombo((String) wardCombo.getSelectedItem(), roomCombo));
        roomCombo.addActionListener(e -> updateBedCombo((String) roomCombo.getSelectedItem(), bedCombo));

        updateDoctorCombo((String) departmentCombo.getSelectedItem(), doctorCombo);
        updateRoomCombo((String) wardCombo.getSelectedItem(), roomCombo);
        updateBedCombo((String) roomCombo.getSelectedItem(), bedCombo);

        JButton submitButton = new JButton("Save & Admit Patient");
        add(new JScrollPane(formPanel), BorderLayout.CENTER);
        add(submitButton, BorderLayout.SOUTH);

        submitButton.addActionListener(e -> {
            try {
                if (nameField.getText().isEmpty() || ageField.getText().isEmpty() || phoneField.getText().isEmpty() || advanceField.getText().isEmpty() || bedCombo.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(this, "Please fill all required fields and select a bed.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }


                Patient p = new Patient(
                        data.getNextPatientId(),
                        nameField.getText(),
                        Integer.parseInt(ageField.getText()),
                        (String) genderCombo.getSelectedItem(),
                        phoneField.getText(),
                        addressField.getText(),
                        (String) bloodGroupCombo.getSelectedItem(),
                        symptomsArea.getText(),
                        (String) departmentCombo.getSelectedItem(),
                        ((String) doctorCombo.getSelectedItem()).split(" ")[0],
                        (String) bedCombo.getSelectedItem(),
                        new Date(),
                        guardianNameField.getText(),
                        (String) paymentCombo.getSelectedItem(),
                        Double.parseDouble(advanceField.getText())
                );


                data.admitPatient(p);
                JOptionPane.showMessageDialog(this, "Patient admitted successfully! ID: " + p.getPatientId(), "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for Age and Advance fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Admission Failed", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
    }

    private void updateDoctorCombo(String department, JComboBox<String> combo) {
        combo.removeAllItems();
        if (department != null) {
            data.doctors.values().stream()
                    .filter(d -> d.getDepartment().equals(department))
                    .forEach(d -> combo.addItem(d.getDoctorId() + " - " + d.getName()));
        }
    }

    private void updateRoomCombo(String ward, JComboBox<String> combo) {
        combo.removeAllItems();
        if (ward != null)
            data.bedsByWard.get(ward).stream().map(b -> b.roomNo).distinct().sorted().forEach(combo::addItem);
    }

    private void updateBedCombo(String room, JComboBox<String> combo) {
        combo.removeAllItems();
        if (room != null)
            data.getVacantBedsByRoom(room).forEach(b -> combo.addItem(b.bedKey));
    }
}