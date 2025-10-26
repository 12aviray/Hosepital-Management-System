package com.hospital.management;
public class Staff extends Person {
    private String staffId, role, department, dutyShift;
    public Staff(String id, String name, int age, String gender, String contact, String address, String role, String dept, String shift) {
        super(name, age, gender, contact, address);
        this.staffId = id; this.role = role; this.department = dept; this.dutyShift = shift;
    }

    public String getStaffId() { return staffId; }
    public String getRole() { return role; }
    public String getDepartment() { return department; }
    public String getDutyShift() { return dutyShift; }
}