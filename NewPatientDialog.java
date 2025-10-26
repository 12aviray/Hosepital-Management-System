package com.hospital.management;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Date;
import java.util.Vector;
import java.util.stream.Collectors;

public class NewPatientDialog extends JDialog {
    private HospitalData data = HospitalData.getInstance();

    public NewPatientDialog(JFrame owner) {
        super(owner, "New Patient Admission Form", true);

        setSize(980, 620);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel mainFormPanel = new JPanel(new GridBagLayout());
        mainFormPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.insets = new Insets(5, 5, 5, 5);
        gbcMain.fill = GridBagConstraints.BOTH;
        gbcMain.weightx = 1.0;


        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        JPanel personalPanel = createSectionPanel("Personal Details", labelFont);
        JPanel medicalPanel = createSectionPanel("Medical Details", labelFont);
        JPanel guardianPanel = createSectionPanel("Guardian & Billing", labelFont);


        addPersonalFields(personalPanel, labelFont, fieldFont);

        JComboBox<String> departmentCombo = new JComboBox<>();
        JComboBox<String> doctorCombo = new JComboBox<>();
        JComboBox<String> wardCombo = new JComboBox<>();
        JComboBox<String> roomCombo = new JComboBox<>();
        JComboBox<String> bedCombo = new JComboBox<>();
        addMedicalFields(medicalPanel, labelFont, fieldFont, departmentCombo, doctorCombo, wardCombo, roomCombo, bedCombo);

        addGuardianFields(guardianPanel, labelFont, fieldFont);


        gbcMain.gridx = 0; gbcMain.gridy = 0;
        mainFormPanel.add(personalPanel, gbcMain);

        gbcMain.gridx = 1; gbcMain.gridy = 0;
        mainFormPanel.add(medicalPanel, gbcMain);

        gbcMain.gridx = 0; gbcMain.gridy = 1; gbcMain.gridwidth = 2;
        mainFormPanel.add(guardianPanel, gbcMain);


        JButton submitButton = new JButton("Save & Admit Patient");
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        submitButton.setBackground(new Color(46, 204, 113));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        buttonPanel.add(submitButton);

        JPanel formWrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        formWrapperPanel.add(new JScrollPane(mainFormPanel));


        add(formWrapperPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);


        JTextField nameField = (JTextField) findComponentByName(personalPanel, "nameField");
        JTextField ageField = (JTextField) findComponentByName(personalPanel, "ageField");
        JComboBox<String> genderCombo = (JComboBox<String>) findComponentByName(personalPanel, "genderCombo");
        JComboBox<String> bloodGroupCombo = (JComboBox<String>) findComponentByName(personalPanel, "bloodGroupCombo");
        JTextField addressField = (JTextField) findComponentByName(personalPanel, "addressField");
        JTextField phoneField = (JTextField) findComponentByName(personalPanel, "phoneField");
        JTextArea symptomsArea = (JTextArea) ((JScrollPane) findComponentByName(medicalPanel, "symptomsArea")).getViewport().getView();
        JTextField guardianNameField = (JTextField) findComponentByName(guardianPanel, "guardianNameField");
        JComboBox<String> paymentCombo = (JComboBox<String>) findComponentByName(guardianPanel, "paymentCombo");
        JTextField advanceField = (JTextField) findComponentByName(guardianPanel, "advanceField");

        departmentCombo.addActionListener(e -> updateDoctorCombo((String) departmentCombo.getSelectedItem(), doctorCombo));
        wardCombo.addActionListener(e -> updateRoomCombo((String) wardCombo.getSelectedItem(), roomCombo));
        roomCombo.addActionListener(e -> updateBedCombo((String) roomCombo.getSelectedItem(), bedCombo));

        updateDoctorCombo((String) departmentCombo.getSelectedItem(), doctorCombo);
        updateRoomCombo((String) wardCombo.getSelectedItem(), roomCombo);
        updateBedCombo((String) roomCombo.getSelectedItem(), bedCombo);

        submitButton.addActionListener(e -> {
            try {
                if (nameField.getText().trim().isEmpty() || ageField.getText().trim().isEmpty() ||
                        phoneField.getText().trim().isEmpty() || advanceField.getText().trim().isEmpty() ||
                        bedCombo.getSelectedItem() == null || doctorCombo.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(this, "Please fill all required fields and select a bed/doctor.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String doctorId = ((String) doctorCombo.getSelectedItem()).split(" ")[0];
                Patient p = new Patient(
                        data.getNextPatientId(), nameField.getText(), Integer.parseInt(ageField.getText()),
                        (String) genderCombo.getSelectedItem(), phoneField.getText(), addressField.getText(),
                        (String) bloodGroupCombo.getSelectedItem(), symptomsArea.getText(),
                        (String) departmentCombo.getSelectedItem(), doctorId,
                        (String) bedCombo.getSelectedItem(), new Date(), guardianNameField.getText(),
                        (String) paymentCombo.getSelectedItem(), Double.parseDouble(advanceField.getText())
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

    private JPanel createSectionPanel(String title, Font labelFont) {
        JPanel panel = new JPanel(new GridBagLayout());
        TitledBorder border = BorderFactory.createTitledBorder(title);
        border.setTitleFont(labelFont);
        panel.setBorder(border);
        return panel;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int y, JLabel label, JComponent component) {
        label.setFont(gbc.insets.bottom == 4 ? ((TitledBorder)panel.getBorder()).getTitleFont() : new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = y;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.1;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.9;
        panel.add(component, gbc);
    }

    private void addPersonalFields(JPanel panel, Font labelFont, Font fieldFont) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 5, 4, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(15);
        nameField.setName("nameField");
        nameField.setFont(fieldFont);
        JTextField ageField = new JTextField(5);
        ageField.setName("ageField");
        ageField.setFont(fieldFont);
        JComboBox<String> genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderCombo.setName("genderCombo");
        genderCombo.setFont(fieldFont);
        JComboBox<String> bloodGroupCombo = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        bloodGroupCombo.setName("bloodGroupCombo");
        bloodGroupCombo.setFont(fieldFont);
        JTextField addressField = new JTextField(15);
        addressField.setName("addressField");
        addressField.setFont(fieldFont);
        JTextField phoneField = new JTextField(15);
        phoneField.setName("phoneField");
        phoneField.setFont(fieldFont);

        int y = 0;
        addField(panel, gbc, y++, new JLabel("Name:"), nameField);
        addField(panel, gbc, y++, new JLabel("Age:"), ageField);
        addField(panel, gbc, y++, new JLabel("Gender:"), genderCombo);
        addField(panel, gbc, y++, new JLabel("Blood Group:"), bloodGroupCombo);
        addField(panel, gbc, y++, new JLabel("Address:"), addressField);
        addField(panel, gbc, y++, new JLabel("Phone:"), phoneField);
    }

    private void addMedicalFields(JPanel panel, Font labelFont, Font fieldFont, JComboBox<String> departmentCombo,
                                  JComboBox<String> doctorCombo, JComboBox<String> wardCombo, JComboBox<String> roomCombo,
                                  JComboBox<String> bedCombo) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 5, 4, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextArea symptomsArea = new JTextArea(2, 15);
        symptomsArea.setFont(fieldFont);
        symptomsArea.setLineWrap(true);
        symptomsArea.setWrapStyleWord(true);
        JScrollPane symptomsScrollPane = new JScrollPane(symptomsArea);
        symptomsScrollPane.setName("symptomsArea");

        departmentCombo.setFont(fieldFont);
        departmentCombo.setModel(new DefaultComboBoxModel<>(new Vector<>(data.doctors.values().stream().map(Doctor::getDepartment).distinct().sorted().collect(Collectors.toList()))));
        doctorCombo.setFont(fieldFont);
        wardCombo.setFont(fieldFont);
        wardCombo.setModel(new DefaultComboBoxModel<>(new Vector<>(data.bedsByWard.keySet())));
        roomCombo.setFont(fieldFont);
        bedCombo.setFont(fieldFont);

        int y = 0;
        addField(panel, gbc, y++, new JLabel("Symptoms:"), symptomsScrollPane);
        addField(panel, gbc, y++, new JLabel("Department:"), departmentCombo);
        addField(panel, gbc, y++, new JLabel("Doctor:"), doctorCombo);
        addField(panel, gbc, y++, new JLabel("Ward:"), wardCombo);
        addField(panel, gbc, y++, new JLabel("Room:"), roomCombo);
        addField(panel, gbc, y++, new JLabel("Bed No:"), bedCombo);
    }

    private void addGuardianFields(JPanel panel, Font labelFont, Font fieldFont) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 5, 4, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField guardianNameField = new JTextField(15);
        guardianNameField.setName("guardianNameField");
        guardianNameField.setFont(fieldFont);
        JComboBox<String> paymentCombo = new JComboBox<>(new String[]{"Cash", "Card", "Mobile Banking", "Insurance"});
        paymentCombo.setName("paymentCombo");
        paymentCombo.setFont(fieldFont);
        JTextField advanceField = new JTextField(10);
        advanceField.setName("advanceField");
        advanceField.setFont(fieldFont);

        int y = 0;
        addField(panel, gbc, y++, new JLabel("Guardian Name:"), guardianNameField);
        addField(panel, gbc, y++, new JLabel("Payment Method:"), paymentCombo);
        addField(panel, gbc, y++, new JLabel("Advance:"), advanceField);
    }

    private Component findComponentByName(Container container, String name) {
        for (Component c : container.getComponents()) {
            if (name.equals(c.getName())) {
                return c;
            }
        }
        for (Component c : container.getComponents()) {
            if (c instanceof Container) {
                Component found = findComponentByName((Container) c, name);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
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
        if (ward != null && data.bedsByWard.containsKey(ward)) {
            data.bedsByWard.get(ward).stream().map(b -> b.roomNo).distinct().sorted().forEach(combo::addItem);
        }
    }
    private void updateBedCombo(String room, JComboBox<String> combo) {
        combo.removeAllItems();
        if (room != null) {
            data.getVacantBedsByRoom(room).forEach(b -> combo.addItem(b.bedKey));
        }
    }
}