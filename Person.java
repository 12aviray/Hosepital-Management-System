package com.hospital.management;

import java.io.Serializable;

public abstract class Person implements Serializable {
    protected String name;
    protected int age;
    protected String gender;
    protected String contactNumber;
    protected String address;

    public Person(String name, int age, String gender, String contactNumber, String address) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    // This method is important for polymorphism
    public String getContactNumber() {
        return contactNumber;
    }
}