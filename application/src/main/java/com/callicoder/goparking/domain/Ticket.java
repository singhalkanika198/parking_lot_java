package com.callicoder.goparking.domain;

import java.time.LocalDateTime;

public class Ticket {
    private int slotNumber;
    private String registrationNumber;
    private String color;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Long parkingCharges;

    public Ticket(int slotNumber, String registrationNumber, String color) {
        this.slotNumber = slotNumber;
        this.registrationNumber = registrationNumber;
        this.color = color;
    }

    public Ticket(int slotNumber, String registrationNumber, String color, LocalDateTime entryTime, LocalDateTime exitTime, Long parkingCharges) {
        this.slotNumber = slotNumber;
        this.registrationNumber = registrationNumber;
        this.color = color;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.parkingCharges = parkingCharges;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getColor() {
        return color;
    }

    public Long getParkingCharges() {
        return parkingCharges;
    }
}
