package com.callicoder.goparking.domain;

import com.callicoder.goparking.exceptions.ParkingLotFullException;
import com.callicoder.goparking.exceptions.SlotAlreadyLeftException;
import com.callicoder.goparking.exceptions.SlotNotFoundException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.callicoder.goparking.utils.MessageConstants.PARKING_CHARGES_PER_HOUR;

public class ParkingLot {
    private final int numSlots;
    private final int numFloors;
    private SortedSet<ParkingSlot> availableSlots = new TreeSet<>();
    private Set<ParkingSlot> occupiedSlots = new HashSet<>();

    public ParkingLot(int numSlots) {
        if (numSlots <= 0) {
            throw new IllegalArgumentException("Number of slots in the Parking Lot must be greater than zero.");
        }

        // Assuming Single floor since only numSlots are specified in the input.
        this.numSlots = numSlots;
        this.numFloors = 1;

        for (int i = 0; i < numSlots; i++) {
            ParkingSlot parkingSlot = new ParkingSlot(i + 1, 1);
            this.availableSlots.add(parkingSlot);
        }
    }


    public synchronized Ticket reserveSlot(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("Car must not be null");
        }

        if (this.isFull()) {
            throw new ParkingLotFullException();
        }

        ParkingSlot nearestSlot = this.availableSlots.first();

        nearestSlot.reserve(car);
        this.availableSlots.remove(nearestSlot);
        this.occupiedSlots.add(nearestSlot);

        return new Ticket(nearestSlot.getSlotNumber(), car.getRegistrationNumber(), car.getColor());
    }

    public ParkingSlot leaveSlot(int slotNumber) {
        if (slotNumber <= 0 || slotNumber > numSlots) {
            throw new SlotNotFoundException(slotNumber);
        }
        Optional<ParkingSlot> slot = this.occupiedSlots
                .stream()
                .filter(parkingSlot -> parkingSlot.getSlotNumber() == slotNumber)
                .findFirst();
        if (slot.isPresent()) {
            slot.get().clear();
            occupiedSlots.remove(slot.get());
            availableSlots.add(slot.get());
            return slot.get();
        } else {
            throw new SlotAlreadyLeftException(slotNumber);
        }
    }

    public boolean isFull() {
        return this.availableSlots.isEmpty();
    }

    public List<String> getCarsRegNumbersWithSameColor(String color) {
        if (color == null) {
            throw new IllegalArgumentException("Color must not be null.");
        }

        return this.occupiedSlots.stream()
                .filter(parkingSlot -> parkingSlot.getCar().getColor().equalsIgnoreCase(color))
                .map(parkingSlot -> parkingSlot.getCar().getRegistrationNumber())
                .collect(Collectors.toList());
    }

    public List<Integer> getSlotNumbersByColor(String color) {
        if (color == null) {
            throw new IllegalArgumentException("Color must not be null");
        }

        return this.occupiedSlots.stream()
                .filter(parkingSlot -> parkingSlot.getCar().getColor().equalsIgnoreCase(color))
                .map(ParkingSlot::getSlotNumber)
                .collect(Collectors.toList());
    }

    public Optional<Integer> getSlotNumberByRegistrationNumber(String registrationNumber) {
        if (registrationNumber == null) {
            throw new IllegalArgumentException("Registration number must not be null");
        }

        return this.occupiedSlots.stream()
                .filter(parkingSlot -> parkingSlot.getCar().getRegistrationNumber()
                        .equalsIgnoreCase(registrationNumber))
                .map(ParkingSlot::getSlotNumber)
                .findFirst();
    }


    public int getNumSlots() {
        return numSlots;
    }

    public int getNumFloors() {
        return numFloors;
    }

    public SortedSet<ParkingSlot> getAvailableSlots() {
        return availableSlots;
    }

    public Set<ParkingSlot> getOccupiedSlots() {
        return occupiedSlots;
    }

    public synchronized Ticket reserveSlotWithTime(Car car, LocalDateTime entryTime) {
        if (car == null) {
            throw new IllegalArgumentException("Car must not be null");
        }

        if (this.isFull()) {
            throw new ParkingLotFullException();
        }

        Duration duration = Duration.between(entryTime, LocalDateTime.now());
        if (duration.toMinutes() < 0) {
            throw new IllegalArgumentException("Entry time of the car can not greater than the current time.");
        } else if (duration.toMinutes() > 1) {
            throw new IllegalArgumentException("The difference between car entry time and current time can not be greater than 1 minute.");
        }

        ParkingSlot nearestSlot = this.availableSlots.first();

        nearestSlot.reserve(car);
        this.availableSlots.remove(nearestSlot);
        this.occupiedSlots.add(nearestSlot);

        return new Ticket(nearestSlot.getSlotNumber(), car.getRegistrationNumber(), car.getColor(), entryTime, null, null);
    }

    public Ticket exitCarWithTime(Car car, LocalDateTime entryTime) {
        Optional<ParkingSlot> slot = this.occupiedSlots
                .stream()
                .filter(parkingSlot -> parkingSlot.getCar().equals(car))
                .findFirst();
        if (slot.isPresent()) {
            LocalDateTime exitTime = LocalDateTime.now();
            Duration duration = Duration.between(entryTime, exitTime);
            if (duration.toMinutes() < 0) {
                throw new IllegalArgumentException("Entry time of the car can not greater than the current time.");
            }
            long parkingCharges = (int) Math.ceil((double) duration.toMinutes() / 60) * PARKING_CHARGES_PER_HOUR;
            Ticket ticket = new Ticket(slot.get().getSlotNumber(), slot.get().getCar().getRegistrationNumber(), slot.get().getCar().getColor(), entryTime, exitTime, parkingCharges);
            slot.get().clear();
            occupiedSlots.remove(slot.get());
            availableSlots.add(slot.get());
            return ticket;
        } else {
            throw new IllegalArgumentException("Car is not found.");
        }
    }
}
