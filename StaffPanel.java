package com.hospital.management;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StaffPanel extends JPanel {
    private CardLayout staffCardLayout;
    private JPanel staffMainPanel;
    private HospitalData data = HospitalData.getInstance();
    private DefaultTableModel staffTableModel;
    private JTable staffTable;
    private JLabel staffListTitle;

    public StaffPanel() {
        staffCardLayout = new CardLayout();
        staffMainPanel = new JPanel(staffCardLayout);

        staffMainPanel.add(createRoleSelectionPanel(), "ROLE_SELECTION");
        staffMainPanel.add(createStaffListPanel(), "STAFF_LIST");

        setLayout(new BorderLayout());
        add(staffMainPanel, BorderLayout.CENTER);

        staffCardLayout.show(staffMainPanel, "ROLE_SELECTION");
    }

    public void switchToRoleSelection() {
        staffCardLayout.show(staffMainPanel, "ROLE_SELECTION");
    }

    public void showNewStaffDialog() {
        StaffManagementDialog dialog = new StaffManagementDialog((JFrame) SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);

    }

    private JPanel createRoleSelectionPanel() {

        JPanel containerPanel = new JPanel(new GridBagLayout());
        containerPanel.setBorder(BorderFactory.createTitledBorder("Staff Management Menu"));
        containerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();


        JPanel roleGrid = new JPanel(new GridLayout(4, 2, 15, 15)); // 4 rows, 2 columns
        roleGrid.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        roleGrid.setBackground(Color.WHITE);

        String[] roles = {"Nurse", "Lab Technician", "Ward Boy", "Billing Officer", "Receptionist", "Security Guard", "Pharmacist", "Ambulance Driver"};
        Color[] colors = {
                new Color(26, 188, 156), new Color(52, 152, 219), new Color(155, 89, 182),
                new Color(241, 196, 15), new Color(230, 126, 34), new Color(45, 52, 54),
                new Color(22, 160, 133), new Color(211, 84, 0)
        };

        for (int i = 0; i < roles.length; i++) {
            String role = roles[i];

            JButton roleButton = createStyledButton(role, colors[i], 18, new Dimension(280, 65));
            roleButton.addActionListener(e -> {
                loadStaffByRole(role);
                staffCardLayout.show(staffMainPanel, "STAFF_LIST");
            });
            roleGrid.add(roleButton);
        }


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        containerPanel.add(roleGrid, gbc);


        JButton addButton = createStyledButton("Add New Staff", new Color(46, 204, 113), 22, new Dimension(600, 65));
        addButton.addActionListener(e -> showNewStaffDialog());


        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 10, 0); // Margin top
        gbc.anchor = GridBagConstraints.CENTER;
        containerPanel.add(addButton, gbc);


        gbc.gridy = 2;
        gbc.weighty = 1.0;
        containerPanel.add(new JLabel(), gbc);

        return containerPanel;
    }

    private JPanel createStaffListPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("⬅ Back to Role Selection");
        backButton.addActionListener(e -> staffCardLayout.show(staffMainPanel, "ROLE_SELECTION"));
        topPanel.add(backButton, BorderLayout.WEST);

        staffListTitle = new JLabel("", SwingConstants.CENTER);
        staffListTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        topPanel.add(staffListTitle, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);

        String[] cols = {"ID", "Name", "Department", "Contact", "Duty Shift"};
        staffTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        staffTable = new JTable(staffTableModel);
        staffTable.setRowHeight(30);
        staffTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        panel.add(new JScrollPane(staffTable), BorderLayout.CENTER);
        return panel;
    }

    private void loadStaffByRole(String role) {
        staffListTitle.setText("Staff List: " + role + "s");
        if (staffTableModel == null) return;
        staffTableModel.setRowCount(0);
        data.staff.values().stream()
                .filter(s -> s.getRole().equals(role))
                .forEach(s -> staffTableModel.addRow(new Object[]{
                        s.getStaffId(), s.getName(), s.getDepartment(),
                        s.getContactNumber(), s.getDutyShift()
                }));
    }

    private JButton createStyledButton(String text, Color color, int fontSize, Dimension size) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(size);
        return button;
    }
}