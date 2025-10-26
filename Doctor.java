package com.hospital.management;
import java.util.Date;
public class Doctor extends Person {
    private String doctorId, specialization, qualification, department, designation, bmdcLicense, dutyHours, status;
    private Date joiningDate;
    public Doctor(String id, String name, int age, String gender, String contact, String address, String spec, String qual, String dept, String desig, String license, String duty, Date joiningDate, String status) {
        super(name, age, gender, contact, address);
        this.doctorId = id; this.specialization = spec; this.qualification = qual; this.department = dept; this.designation = desig; this.bmdcLicense = license; this.dutyHours = duty; this.joiningDate = joiningDate; this.status = status;
    }

    public String getDoctorId() { return doctorId; }
    public String getSpecialization() { return specialization; }
    public String getQualification() { return qualification; }
    public String getDepartment() { return department; }
    public String getDesignation() { return designation; }
    public String getBmdcLicense() { return bmdcLicense; }
    public String getDutyHours() { return dutyHours; }
    public String getStatus() { return status; }
    public Date getJoiningDate() { return joiningDate; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getContactNumber() { return contactNumber; }
    public String getAddress() { return address; }
}