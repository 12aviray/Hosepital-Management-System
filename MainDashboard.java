package com.hospital.management;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainDashboard extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);

    public MainDashboard() {
        setTitle("IIT HOSPITAL - Management System");
        setSize(1366, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(45, 52, 54));
        menuBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        Font menuFont = new Font("Segoe UI", Font.BOLD, 16);
        Color menuForegroundColor = Color.WHITE;
        Border menuBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(45, 52, 54), 2),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)
        );


        PatientPanel patientPanel = new PatientPanel();
        DoctorPanel doctorPanel = new DoctorPanel();
        StaffPanel staffPanel = new StaffPanel();
        mainPanel.add(new HomePanel(cardLayout, mainPanel), "Dashboard");
        mainPanel.add(patientPanel, "Patient Management");
        mainPanel.add(doctorPanel, "Doctor Management");
        mainPanel.add(staffPanel, "Staff Management");
        mainPanel.add(new BedStatusPanel(), "Bed Status");
        mainPanel.add(new AmbulancePanel(), "Ambulance Status");
        mainPanel.add(new ReportsPanel(), "Reports");
        mainPanel.add(new AuthorityPanel(), "Authority");


        JMenu dashboardMenu = new JMenu("Dashboard");
        JMenu patientMenu = new JMenu("Patient Management");
        JMenu doctorMenu = new JMenu("Doctor Management");
        JMenu staffMenu = new JMenu("Staff Management");
        JMenu bedStatusMenu = new JMenu("Bed Status");
        JMenu ambulanceMenu = new JMenu("Ambulance Status");
        JMenu reportsMenu = new JMenu("Reports");
        JMenu authorityMenu = new JMenu("Authority");


        styleMenu(dashboardMenu, menuFont, new Color(99, 110, 114), menuForegroundColor, menuBorder);
        styleMenu(patientMenu, menuFont, new Color(26, 188, 156), menuForegroundColor, menuBorder);
        styleMenu(doctorMenu, menuFont, new Color(52, 152, 219), menuForegroundColor, menuBorder);
        styleMenu(staffMenu, menuFont, new Color(155, 89, 182), menuForegroundColor, menuBorder);
        styleMenu(bedStatusMenu, menuFont, new Color(241, 196, 15), menuForegroundColor, menuBorder);
        styleMenu(ambulanceMenu, menuFont, new Color(46, 204, 113), menuForegroundColor, menuBorder);
        styleMenu(reportsMenu, menuFont, new Color(230, 126, 34), menuForegroundColor, menuBorder);
        styleMenu(authorityMenu, menuFont, new Color(231, 76, 60), menuForegroundColor, menuBorder);


        JMenuItem admitPatientItem = new JMenuItem("Admit New Patient");
        JMenuItem viewPatientsItem = new JMenuItem("View Admitted Patients");
        JMenuItem viewDischargedItem = new JMenuItem("View Discharged Patients");
        patientMenu.add(admitPatientItem);
        patientMenu.add(viewPatientsItem);
        patientMenu.add(viewDischargedItem);

        JMenuItem viewDoctorsItem = new JMenuItem("View Doctors");
        JMenuItem addDoctorItem = new JMenuItem("Add New Doctor");
        doctorMenu.add(viewDoctorsItem);
        doctorMenu.add(addDoctorItem);

        JMenuItem viewStaffItem = new JMenuItem("View Staff by Role");
        JMenuItem addStaffItem = new JMenuItem("Add New Staff");
        staffMenu.add(viewStaffItem);
        staffMenu.add(addStaffItem);

        menuBar.add(dashboardMenu);
        menuBar.add(patientMenu);
        menuBar.add(doctorMenu);
        menuBar.add(staffMenu);
        menuBar.add(bedStatusMenu);
        menuBar.add(ambulanceMenu);
        menuBar.add(reportsMenu);
        menuBar.add(authorityMenu);

        setJMenuBar(menuBar);
        add(mainPanel);


        addMenuClickListener(dashboardMenu, "Dashboard");
        addMenuClickListener(bedStatusMenu, "Bed Status");
        addMenuClickListener(ambulanceMenu, "Ambulance Status");
        addMenuClickListener(reportsMenu, "Reports");
        addMenuClickListener(authorityMenu, "Authority");


        admitPatientItem.addActionListener(e -> {
            cardLayout.show(mainPanel, "Patient Management");
            patientPanel.showNewPatientDialog();
        });
        viewPatientsItem.addActionListener(e -> {
            cardLayout.show(mainPanel, "Patient Management");
            patientPanel.switchToCard("ADMITTED_LIST");
        });
        viewDischargedItem.addActionListener(e -> {
            cardLayout.show(mainPanel, "Patient Management");
            patientPanel.switchToCard("DISCHARGED_LIST");
        });
        patientMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "Patient Management");
                patientPanel.switchToCard("HOME");
            }
        });

        viewDoctorsItem.addActionListener(e -> {
            cardLayout.show(mainPanel, "Doctor Management");
            doctorPanel.switchToCard("DEPARTMENT_SELECTION");
        });
        addDoctorItem.addActionListener(e -> {
            cardLayout.show(mainPanel, "Doctor Management");
            doctorPanel.showNewDoctorDialog();
        });
        doctorMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "Doctor Management");
                doctorPanel.switchToCard("HOME");
            }
        });


        viewStaffItem.addActionListener(e -> {
            cardLayout.show(mainPanel, "Staff Management");
            staffPanel.switchToRoleSelection();
        });
        addStaffItem.addActionListener(e -> {
            cardLayout.show(mainPanel, "Staff Management");
            staffPanel.showNewStaffDialog();
        });
        staffMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "Staff Management");
                staffPanel.switchToRoleSelection();
            }
        });


        cardLayout.show(mainPanel, "Dashboard");
    }

    private void addMenuClickListener(JMenu menu, String panelName) {
        menu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (menu.getItemCount() == 0) {
                    cardLayout.show(mainPanel, panelName);
                }
            }
        });
    }

    private void styleMenu(JMenu menu, Font font, Color bgColor, Color fgColor, Border border) {
        menu.setFont(font);
        menu.setBackground(bgColor);
        menu.setForeground(fgColor);
        menu.setBorder(border);
        menu.setOpaque(true);
        menu.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPopupMenu popupMenu = menu.getPopupMenu();
        popupMenu.setBackground(new Color(223, 230, 233));
        popupMenu.setBorder(BorderFactory.createLineBorder(new Color(99, 110, 114), 1));
    }
}