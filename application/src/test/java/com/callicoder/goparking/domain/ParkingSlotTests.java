package com.callicoder.goparking.domain;

import com.callicoder.goparking.exceptions.SlotNotAvailableException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParkingSlotTests {

    @Test
    public void reserve_shouldReserveSlot() {
        ParkingSlot parkingSlot = new ParkingSlot(1, 1);
        Car car = new Car("KA01HQ4669", "Red");
        assertTrue(parkingSlot.isAvailable());
        assertTrue(parkingSlot.reserve(car));
        assertFalse(parkingSlot.isAvailable());
    }

    @Test
    public void reserveUnavailableSlot_shouldThrowError() {
        ParkingSlot parkingSlot = new ParkingSlot(3, 1);
        Car car = new Car("TN02HAS9123", "Black");
        parkingSlot.reserve(car);

        assertThrows(SlotNotAvailableException.class, () -> parkingSlot.reserve(car));
    }

    @Test
    public void reserveSlotWithMissingCar_shouldThrowError() {
        ParkingSlot parkingSlot = new ParkingSlot(5, 1);
        assertThrows(IllegalArgumentException.class, () -> parkingSlot.reserve(null));
    }

    @Test
    public void compareTo_shouldCompareSlotsBasedOnSlotNumber() {
        ParkingSlot parkingSlot1 = new ParkingSlot(12, 1);
        ParkingSlot parkingSlot2 = new ParkingSlot(15, 2);

        assertTrue(parkingSlot1.compareTo(parkingSlot2) < 0);
    }

    @Test
    public void equals_equalityWithSameObject_shouldReturnTrue() {
        ParkingSlot parkingSlot1 = new ParkingSlot(10, 3);

        assertTrue(parkingSlot1.equals(parkingSlot1));
    }

    @Test
    public void equals_shouldCheckEqualityBasedOnSlotNumber() {
        ParkingSlot parkingSlot1 = new ParkingSlot(10, 3);
        ParkingSlot parkingSlot2 = new ParkingSlot(10, 3);

        assertTrue(parkingSlot1.equals(parkingSlot2));
    }

    @Test
    public void equals_functionArgumentIsNull_shouldReturnFalse() {
        ParkingSlot parkingSlot1 = new ParkingSlot(10, 3);
        assertFalse(parkingSlot1.equals(null));
    }

    @Test
    public void equals_functionArgumentAsObjectOtherThanParkingSlotInstance_shouldReturnFalse() {
        ParkingSlot parkingSlot1 = new ParkingSlot(10, 3);
        assertFalse(parkingSlot1.equals(new ParkingLot(9)));
    }

    @Test
    public void isAvailable_carIsNull_shouldReturnTrue() {
        ParkingSlot parkingSlot = new ParkingSlot(1, 1);
        assertTrue(parkingSlot.isAvailable());
    }

    @Test
    public void isAvailable_carIsNotNull_shouldReturnFalse() {
        ParkingSlot parkingSlot = new ParkingSlot(1, 1);
        parkingSlot.setCar(new Car("", "black"));
        assertFalse(parkingSlot.isAvailable());
    }

    @Test
    public void clear_shouldClearSlot() {
        ParkingSlot parkingSlot = new ParkingSlot(2, 1);
        parkingSlot.setCar(new Car("TN02HAS9123", "black"));
        parkingSlot.clear();

        assertTrue(parkingSlot.clear());
        assertNull(parkingSlot.getCar());
    }
}
