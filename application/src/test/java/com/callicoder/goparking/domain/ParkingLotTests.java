package com.callicoder.goparking.domain;

import com.callicoder.goparking.exceptions.ParkingLotFullException;
import com.callicoder.goparking.exceptions.SlotAlreadyLeftException;
import com.callicoder.goparking.exceptions.SlotNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ParkingLotTests {
    @Test
    public void createParkingSlot_shouldCreateParkingLotWithFreeSlots() {
        ParkingLot parkingLot = new ParkingLot(10);

        assertEquals(parkingLot.getAvailableSlots().size(), 10);
        assertTrue(parkingLot.getOccupiedSlots().isEmpty());
        assertEquals(parkingLot.getNumFloors(), 1);
        assertEquals(parkingLot.getNumSlots(), 10);
    }

    @Test
    public void createParkingLotWithIllegalNumSlots_shouldThrowError() {
        assertThrows(IllegalArgumentException.class, () -> {
            ParkingLot parkingLot = new ParkingLot(0);
        });
    }

    @Test
    public void reserveSlot_carObjectIsNull_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            ParkingLot parkingLot = new ParkingLot(0);
            parkingLot.reserveSlot(null);
        });
    }

    @Test
    public void reserveSlotWithFullParking_shouldThrowError() {
        ParkingLot parkingLot = new ParkingLot(1);
        Car car = new Car("TN02BH1145", "Red");
        parkingLot.reserveSlot(car);

        Car car2 = new Car("BH89JD8765", "Black");
        assertThrows(ParkingLotFullException.class, () -> parkingLot.reserveSlot(car2));
    }

    @Test
    public void isFull_shouldCheckIfParkingLotIsFull() {
        ParkingLot parkingLot = new ParkingLot(2);

        Car car = new Car("KA02TH1992", "White");
        Car car2 = new Car("BH89JD8765", "Black");

        parkingLot.reserveSlot(car);
        parkingLot.reserveSlot(car2);

        assertTrue(parkingLot.isFull());
    }

    @Test
    public void reserveSlot_shouldReserveNearestSlot() {
        ParkingLot parkingLot = new ParkingLot(20);
        Car car = new Car("KA02TH1992", "White");
        ParkingSlot nearestSlot = parkingLot.getAvailableSlots().first();
        int numAvailableSlots = parkingLot.getAvailableSlots().size();
        int numOccupiedSlots = parkingLot.getOccupiedSlots().size();

        Ticket ticket = parkingLot.reserveSlot(car);

        assertEquals(nearestSlot.getFloorNumber(), 1);
        assertEquals(ticket.getSlotNumber(), nearestSlot.getSlotNumber());
        assertEquals(nearestSlot.getCar(), car);
        assertEquals(parkingLot.getAvailableSlots().size(), numAvailableSlots - 1);
        assertEquals(parkingLot.getOccupiedSlots().size(), numOccupiedSlots + 1);
        assertEquals(ticket.getSlotNumber(), nearestSlot.getSlotNumber());
        assertEquals(ticket.getRegistrationNumber(), car.getRegistrationNumber());
        assertEquals(ticket.getColor(), car.getColor());
    }

    @Test
    public void leaveSlotWithNonExistingSlotNo_shouldThrowError() {
        ParkingLot parkingLot = new ParkingLot(6);
        assertThrows(SlotNotFoundException.class, () -> parkingLot.leaveSlot(99));
    }

    @Test
    public void leaveSlot_shouldClearSlot() {
        ParkingLot parkingLot = new ParkingLot(4);
        Car car = new Car("AN82CH1235", "Green");
        Ticket ticket1 = parkingLot.reserveSlot(car);

        int numAvailableSlots = parkingLot.getAvailableSlots().size();
        int numOccupiedSlots = parkingLot.getOccupiedSlots().size();

        ParkingSlot slot = parkingLot.leaveSlot(ticket1.getSlotNumber());

        assertNull(slot.getCar());
        assertEquals(slot.getFloorNumber(), 1);
        assertEquals(slot.getSlotNumber(), 1);
        assertEquals(parkingLot.getAvailableSlots().size(), numAvailableSlots + 1);
        assertEquals(parkingLot.getOccupiedSlots().size(), numOccupiedSlots - 1);
    }

    @Test
    public void leaveSlot_noAlreadyOccupiedSlot_shouldThrowError() {
        assertThrows(SlotAlreadyLeftException.class, () -> {
            ParkingLot parkingLot = new ParkingLot(9);
            parkingLot.leaveSlot(8);
        });
    }

    @Test
    public void isFull_slotsAvailable_shouldReturnFalse() {
        ParkingLot parkingLot = new ParkingLot(2);
        assertFalse(parkingLot.isFull());
    }

    @Test
    public void isFull_noSlotsAvailable_shouldReturnTrue() {
        ParkingLot parkingLot = new ParkingLot(1);
        parkingLot.reserveSlot(new Car("AN82CH1235", "green"));
        assertTrue(parkingLot.isFull());
    }

    @Test
    public void getRegistrationNumbersByColor_shouldReturnRegistrationNumbers() {
        ParkingLot parkingLot = new ParkingLot(10);

        Car car1 = new Car("KA01HQ4669", "White");
        Car car2 = new Car("HY02OS7867", "Black");
        Car car3 = new Car("BH09AB2397", "Black");
        Car car4 = new Car("TN03A13392", "Black");

        parkingLot.reserveSlot(car1);
        parkingLot.reserveSlot(car2);
        parkingLot.reserveSlot(car3);
        parkingLot.reserveSlot(car4);

        List<String> registrationNumbers = parkingLot.getCarsRegNumbersWithSameColor("Black");

        assertIterableEquals(registrationNumbers, Arrays.asList("HY02OS7867", "BH09AB2397", "TN03A13392"));
    }

    @Test
    public void getRegistrationNumbersByColor_colorIsNull_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            ParkingLot parkingLot = new ParkingLot(9);
            parkingLot.getCarsRegNumbersWithSameColor(null);
        });
    }

    @Test
    public void getSlotNumbersByColor_colorIsNUll_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            ParkingLot parkingLot = new ParkingLot(9);
            parkingLot.getSlotNumbersByColor(null);
        });
    }

    @Test
    public void getSlotNumbersByColor_shouldReturnSlotNos() {
        ParkingLot parkingLot = new ParkingLot(10);

        Car car1 = new Car("KA01HQ4669", "Red");
        Car car2 = new Car("HY02OS7867", "Blue");
        Car car3 = new Car("BH09AB2397", "Green");
        Car car4 = new Car("TN03A13392", "Red");

        parkingLot.reserveSlot(car1);
        parkingLot.reserveSlot(car2);
        parkingLot.reserveSlot(car3);
        parkingLot.reserveSlot(car4);

        List<Integer> slotNumbers = parkingLot.getSlotNumbersByColor("Red");

        assertIterableEquals(slotNumbers, Arrays.asList(1, 4));
    }

    @Test
    public void getSlotNumberByRegistrationNumber_shouldReturnSlotNo() {
        ParkingLot parkingLot = new ParkingLot(5);

        Car car1 = new Car("KA01HQ4669", "Black");
        Car car2 = new Car("HY02OS7867", "White");

        parkingLot.reserveSlot(car1);
        parkingLot.reserveSlot(car2);

        Optional<Integer> slotNumber = parkingLot.getSlotNumberByRegistrationNumber("KA01HQ4669");

        assertTrue(slotNumber.isPresent());
        assertEquals(slotNumber.get().intValue(), 1);
    }

    @Test
    public void getSlotNumberWithWrongRegistrationNo_shouldReturnNoSlot() {
        ParkingLot parkingLot = new ParkingLot(4);
        Car car = new Car("KA01HQ4669", "Black");
        parkingLot.reserveSlot(car);

        Optional<Integer> slotNumber = parkingLot.getSlotNumberByRegistrationNumber("GH01KQ7643");

        assertTrue(!slotNumber.isPresent());
    }

    @Test
    public void getSlotNumberWithWrongRegistrationNo_regNoIsNull_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            ParkingLot parkingLot = new ParkingLot(9);
            parkingLot.getSlotNumberByRegistrationNumber(null);
        });
    }
}
