package com.hospital.management;

import java.io.Serializable;
import java.util.Date;

public class Authority extends Person implements Serializable {
    private String authorityId;
    private String designation;
    private String department;
    private String email;
    private Date joiningDate;
    private String status;

    public Authority(String authorityId, String name, int age, String gender, String contactNumber, String address,
                     String designation, String department, String email, Date joiningDate, String status) {
        super(name, age, gender, contactNumber, address);
        this.authorityId = authorityId;
        this.designation = designation;
        this.department = department;
        this.email = email;
        this.joiningDate = joiningDate;
        this.status = status;
    }


    public String getAuthorityId() { return authorityId; }
    public String getDesignation() { return designation; }
    public String getDepartment() { return department; }
    public String getEmail() { return email; }
    public Date getJoiningDate() { return joiningDate; }
    public String getStatus() { return status; }


    public void setDesignation(String designation) { this.designation = designation; }
    public void setDepartment(String department) { this.department = department; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public void setEmail(String email) { this.email = email; }
    public void setStatus(String status) { this.status = status; }
    public void setAddress(String address) { this.address = address; }
}