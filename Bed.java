package com.hospital.management;
import java.io.Serializable;
public class Bed implements Serializable {
    String bedKey, roomNo, wardType;
    boolean isVacant = true;
    public Bed(String roomNo, String bedKey, String wardType) {
        this.roomNo = roomNo; this.bedKey = bedKey; this.wardType = wardType;
    }
}