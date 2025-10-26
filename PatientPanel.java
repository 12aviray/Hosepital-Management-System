package com.hospital.management;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor; // <-- এই লাইনটি যোগ করা হয়েছে
import javax.swing.AbstractCellEditor;   // <-- এই লাইনটিও যোগ করা হয়েছে
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

public class PatientPanel extends JPanel {
    private CardLayout patientCardLayout;
    private JPanel patientMainPanel;
    private HospitalData data = HospitalData.getInstance();

    private DefaultTableModel admittedTableModel;
    private JTable admittedPatientTable;
    private JTextField searchField;

    private DefaultTableModel dischargedTableModel;
    private JTable dischargedPatientTable;

    public PatientPanel() {
        patientCardLayout = new CardLayout();
        patientMainPanel = new JPanel(patientCardLayout);

        JPanel homePanel = createPatientHomePanel();
        JPanel admittedPanel = createAdmittedPatientsPanel();
        JPanel dischargedPanel = createDischargedPatientsPanel();

        patientMainPanel.add(homePanel, "HOME");
        patientMainPanel.add(admittedPanel, "ADMITTED_LIST");
        patientMainPanel.add(dischargedPanel, "DISCHARGED_LIST");

        setLayout(new BorderLayout());
        add(patientMainPanel, BorderLayout.CENTER);

        patientCardLayout.show(patientMainPanel, "HOME");
    }

    public void switchToCard(String cardName) {
        patientCardLayout.show(patientMainPanel, cardName);
    }

    private JPanel createPatientHomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel title = new JLabel("Patient Management Menu");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        panel.add(title, gbc);
        gbc.gridy++;
        JButton admitButton = createStyledButton("Admit New Patient", new Color(26, 188, 156));
        admitButton.addActionListener(e -> showNewPatientDialog());
        panel.add(admitButton, gbc);
        gbc.gridy++;
        JButton viewAdmittedButton = createStyledButton("View Admitted Patients", new Color(52, 152, 219));
        viewAdmittedButton.addActionListener(e -> patientCardLayout.show(patientMainPanel, "ADMITTED_LIST"));
        panel.add(viewAdmittedButton, gbc);
        gbc.gridy++;
        JButton viewDischargedButton = createStyledButton("View Discharged Patients", new Color(230, 126, 34));
        viewDischargedButton.addActionListener(e -> patientCardLayout.show(patientMainPanel, "DISCHARGED_LIST"));
        panel.add(viewDischargedButton, gbc);
        return panel;
    }

    private JPanel createAdmittedPatientsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Patient List"));

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("⬅ Back to Patient Menu");
        backButton.addActionListener(e -> patientCardLayout.show(patientMainPanel, "HOME"));
        topPanel.add(backButton, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchField = new JTextField(25);
        JButton searchBtn = new JButton("Search");
        searchPanel.add(new JLabel("Search by Name/ID/Contact:"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        topPanel.add(searchPanel, BorderLayout.EAST);

        String[] cols = {"ID", "Name", "Age", "Gender", "Contact", "Department", "Bed", "Doctor", "Action"};
        admittedTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8;
            }
        };
        admittedPatientTable = new JTable(admittedTableModel);
        admittedPatientTable.setRowHeight(30);
        admittedPatientTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        admittedPatientTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        admittedPatientTable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));

        loadAdmittedPatientData("");

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(admittedPatientTable), BorderLayout.CENTER);

        searchBtn.addActionListener(e -> loadAdmittedPatientData(searchField.getText()));

        admittedPatientTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int col = admittedPatientTable.columnAtPoint(evt.getPoint());
                if (evt.getClickCount() == 2 && col != 8) {
                    int row = admittedPatientTable.getSelectedRow();
                    if (row >= 0) {
                        int patientId = (int) admittedPatientTable.getValueAt(row, 0);
                        showPatientDetails(patientId);
                    }
                }
            }
        });

        return panel;
    }

    private JPanel createDischargedPatientsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Discharged Patients History"));

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("⬅ Back to Patient Menu");
        backButton.addActionListener(e -> patientCardLayout.show(patientMainPanel, "HOME"));
        topPanel.add(backButton, BorderLayout.WEST);

        String[] cols = {"ID", "Name", "Age", "Contact", "Department", "Admission Date"};
        dischargedTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dischargedPatientTable = new JTable(dischargedTableModel);
        dischargedPatientTable.setRowHeight(30);
        dischargedPatientTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        loadDischargedPatientData();

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(dischargedPatientTable), BorderLayout.CENTER);

        dischargedPatientTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = dischargedPatientTable.getSelectedRow();
                    if (row >= 0) {
                        int patientId = (int) dischargedPatientTable.getValueAt(row, 0);
                        showPatientDetails(patientId);
                    }
                }
            }
        });

        return panel;
    }

    public void showNewPatientDialog() {
        NewPatientDialog dialog = new NewPatientDialog((JFrame) SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
        loadAdmittedPatientData("");
    }

    private void loadAdmittedPatientData(String filter) {
        if (admittedTableModel == null) return;
        admittedTableModel.setRowCount(0);
        data.patients.values().stream()
                .filter(p -> !p.isDischarged())
                .filter(p -> filter == null || filter.isEmpty() ||
                        p.getName().toLowerCase().contains(filter.toLowerCase()) ||
                        String.valueOf(p.getPatientId()).contains(filter) ||
                        p.getContactNumber().contains(filter))
                .forEach(p -> {
                    String docName = data.doctors.containsKey(p.getAssignedDoctorId()) ? data.doctors.get(p.getAssignedDoctorId()).getName() : "N/A";
                    admittedTableModel.addRow(new Object[]{p.getPatientId(), p.getName(), p.getAge(), p.getGender(), p.getContactNumber(), p.getDepartment(), p.getBedKey(), docName, "Discharge"});
                });
    }

    private void loadDischargedPatientData() {
        if (dischargedTableModel == null) return;
        dischargedTableModel.setRowCount(0);
        data.patients.values().stream()
                .filter(Patient::isDischarged)
                .forEach(p -> dischargedTableModel.addRow(new Object[]{
                        p.getPatientId(), p.getName(), p.getAge(), p.getContactNumber(), p.getDepartment(),
                        new SimpleDateFormat("dd-MMM-yyyy").format(p.getAdmissionDate())
                }));
    }

    private void showPatientDetails(int patientId) {
        Patient p = data.patients.get(patientId);
        if (p == null) return;
        Doctor d = data.doctors.get(p.getAssignedDoctorId());
        JTextArea textArea = new JTextArea(22, 55);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        StringBuilder details = new StringBuilder();
        details.append("===== PATIENT DETAILS (ID: ").append(p.getPatientId()).append(") =====\n\n");
        details.append(String.format("%-20s: %s\n", "Name", p.getName()));
        details.append(String.format("%-20s: %d\n", "Age", p.getAge()));
        details.append(String.format("%-20s: %s\n", "Status", p.isDischarged() ? "Discharged" : "Admitted"));
        details.append(String.format("%-20s: %s\n", "Gender", p.getGender()));
        details.append(String.format("%-20s: %s\n", "Blood Group", p.getBloodGroup()));
        details.append(String.format("%-20s: %s\n", "Contact", p.getContactNumber()));
        details.append(String.format("%-20s: %s\n", "Address", p.getAddress()));
        details.append("\n===== MEDICAL DETAILS =====\n\n");
        details.append(String.format("%-20s: %s\n", "Symptoms", p.getSymptoms()));
        details.append(String.format("%-20s: %s\n", "Department", p.getDepartment()));
        details.append(String.format("%-20s: %s (ID: %s)\n", "Assigned Doctor", d != null ? d.getName() : "N/A", p.getAssignedDoctorId()));
        details.append(String.format("%-20s: %s\n", "Bed No", p.getBedKey()));
        details.append(String.format("%-20s: %s\n", "Admission Date", new SimpleDateFormat("dd-MMM-yyyy").format(p.getAdmissionDate())));
        details.append("\n===== GUARDIAN & BILLING =====\n\n");
        details.append(String.format("%-20s: %s\n", "Guardian Name", p.getGuardianName()));
        details.append(String.format("%-20s: %s\n", "Payment Method", p.getPaymentMethod()));
        details.append(String.format("%-20s: %.2f\n", "Advance Payment", p.getAdvancePayment()));
        textArea.setText(details.toString());
        textArea.setCaretPosition(0);
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Patient Full History", JOptionPane.INFORMATION_MESSAGE);
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

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setBackground(new Color(231, 76, 60));
            setForeground(Color.WHITE);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private JButton button;
        private String label;
        private boolean isPushed;
        private int row;
        private JTable table;

        public ButtonEditor(JCheckBox checkBox) {
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(this);
            button.setFont(new Font("Segoe UI", Font.BOLD, 12));
            button.setBackground(new Color(231, 76, 60));
            button.setForeground(Color.WHITE);
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.table = table;
            this.row = row;
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            return label;
        }

        public void actionPerformed(ActionEvent e) {
            fireEditingStopped();
            int patientId = (int) table.getValueAt(row, 0);
            String patientName = (String) table.getValueAt(row, 1);

            int confirm = JOptionPane.showConfirmDialog(
                    PatientPanel.this,
                    "Are you sure you want to discharge patient: " + patientName + " (ID: " + patientId + ")?",
                    "Confirm Discharge",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                data.dischargePatient(patientId);
                loadAdmittedPatientData(searchField.getText());
                loadDischargedPatientData();
                JOptionPane.showMessageDialog(PatientPanel.this, "Patient discharged successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}