package com.hospital.management;

import java.io.Serializable;

public class Ambulance implements Serializable {
    String number;
    String status;

    public Ambulance(String number, String status) {
        this.number = number;
        this.status = status;
    }
}