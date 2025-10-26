package com.hospital.management;
import java.util.Date;
public class Patient extends Person {
    private int patientId;
    private String bloodGroup, symptoms, department, assignedDoctorId, bedKey, guardianName, paymentMethod;
    private Date admissionDate;
    private double advancePayment;
    private boolean isDischarged = false;
    public Patient(int id, String name, int age, String gender, String contact, String address, String blood, String symp, String dept, String docId, String bed, Date date, String guardName, String payMethod, double advance) {
        super(name, age, gender, contact, address);
        this.patientId = id; this.bloodGroup = blood; this.symptoms = symp; this.department = dept; this.assignedDoctorId = docId; this.bedKey = bed; this.admissionDate = date; this.guardianName = guardName; this.paymentMethod = payMethod; this.advancePayment = advance;
    }

    public int getPatientId() { return patientId; }
    public String getBloodGroup() { return bloodGroup; }
    public String getDepartment() { return department; }
    public String getAssignedDoctorId() { return assignedDoctorId; }
    public String getBedKey() { return bedKey; }
    public boolean isDischarged() { return isDischarged; }
    public void setDischarged(boolean d) { isDischarged = d; }

    public String getContactNumber() { return contactNumber; }
    public Date getAdmissionDate() { return admissionDate; }
    public String getGuardianName() { return guardianName; }
    public double getAdvancePayment() { return advancePayment; }
    public String getSymptoms() { return symptoms; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getAddress() { return address; }
    public String getPaymentMethod() { return paymentMethod; }
}