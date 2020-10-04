package com.callicoder.goparking.handler;

import com.callicoder.goparking.domain.Car;
import com.callicoder.goparking.domain.ParkingLot;
import com.callicoder.goparking.domain.Ticket;
import com.callicoder.goparking.exceptions.ParkingLotFullException;
import com.callicoder.goparking.exceptions.SlotAlreadyLeftException;
import com.callicoder.goparking.exceptions.SlotNotFoundException;
import com.callicoder.goparking.utils.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.callicoder.goparking.utils.MessageConstants.*;

/**
 * Handles the commands related to {@link ParkingLot}.
 */
public class ParkingLotCommandHandler {
    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    private ParkingLot parkingLot;

    /**
     * It creates a parking lot with given number of slots.
     *
     * @param numSlots to be added in parking lot.
     */
    public void createParkingLot(int numSlots) {
        try {
            if (isParkingLotCreated()) {
                System.out.println(PARKING_LOT_ALREADY_CREATED);
                return;
            }
            parkingLot = new ParkingLot(numSlots);
            System.out.println(String.format(PARKING_LOT_CREATED_MSG, parkingLot.getNumSlots()));
        } catch (IllegalArgumentException ex) {
            // Error log for developers can be printed here.
            System.out.println("Bad input: " + ex.getMessage());
        }
    }

    /**
     * It parks a car with given registration number and color in parking lot.
     *
     * @param registrationNumber of car.
     * @param color              of car.
     */
    public void park(String registrationNumber, String color) {
        try {
            if (!isParkingLotCreated()) {
                System.out.println(PARKING_LOT_NOT_CREATED);
                return;
            }
            Car car = new Car(registrationNumber, color);
            boolean isCarAlreadyParked = isCarAlreadyParked(car);
            if (isCarAlreadyParked) {
                System.out.println(String.format(DUPLICATE_CAR_MESSAGE, registrationNumber));
                return;
            }
            Ticket ticket = parkingLot.reserveSlot(car);
            System.out.println(String.format(PARKING_SLOT_ALLOCATED_MSG, ticket.getSlotNumber()));
        } catch (IllegalArgumentException ex) {
            // Error log for developers can be printed here.
            System.out.println("Bad input: " + ex.getMessage());
        } catch (ParkingLotFullException ex) {
            // Error log for developers can be printed here.
            System.out.println(PARKING_LOT_FULL_MSG);
        }
    }

    /**
     * It checks if a given car is present inside parking lot.
     *
     * @param car to be checked.
     * @return true if the given car is already inside parking lot else false.
     */
    public boolean isCarAlreadyParked(Car car) {
        List<Car> cars = parkingLot.getOccupiedSlots().stream().map(slot -> slot.getCar()).collect(Collectors.toList());
        if (cars.contains(car)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * It prints the current snapshot of parking lot.
     */
    public void status() {
        if (!isParkingLotCreated()) {
            System.out.println(PARKING_LOT_NOT_CREATED);
            return;
        }

        System.out.println(SLOT_NO + "    " + REGISTRATION_NO + "    " + Color);
        parkingLot.getOccupiedSlots().forEach(parkingSlot -> {
            System.out.println(
                    StringUtils.rightPadSpaces(Integer.toString(parkingSlot.getSlotNumber()), SLOT_NO.length()) + "    " +
                            StringUtils.rightPadSpaces(parkingSlot.getCar().getRegistrationNumber(), REGISTRATION_NO.length()) + "    " +
                            parkingSlot.getCar().getColor());
        });
    }

    /**
     * It vacates a particular slot.
     *
     * @param slotNumber to vacate.
     */
    public void leaveParkingSpot(int slotNumber) {
        try {
            if (!isParkingLotCreated()) {
                System.out.println(PARKING_LOT_NOT_CREATED);
                return;
            }
            parkingLot.leaveSlot(slotNumber);
            System.out.println(String.format(PARKING_SLOT_VACATED_MSG, slotNumber));
        } catch (SlotNotFoundException | SlotAlreadyLeftException ex) {
            System.out.println("Bad input: " + ex.getMessage());
        }
    }

    /**
     * It prints the registration number of all the cars having color same as argument passed to this function.
     * If no car is found with this color, {@link com.callicoder.goparking.utils.MessageConstants#NOT_FOUND} is printed.
     * If parking lot is not created, {@link com.callicoder.goparking.utils.MessageConstants#PARKING_LOT_ALREADY_CREATED} is printed.
     *
     * @param color of the car.
     */
    public void carsRegNumbersWithSameColor(String color) {
        try {
            if (!isParkingLotCreated()) {
                System.out.println(PARKING_LOT_NOT_CREATED);
                return;
            }
            List<String> carsRegNumberWithSameColorList = parkingLot.getCarsRegNumbersWithSameColor(color);
            if (carsRegNumberWithSameColorList.isEmpty()) {
                System.out.println(NOT_FOUND);
            } else {
                String carsRegNumbersString = carsRegNumberWithSameColorList
                        .stream()
                        .collect(Collectors.joining(", "));
                System.out.println(carsRegNumbersString);
            }
        } catch (IllegalArgumentException ex) {
            // Error log for developers can be printed here.
            System.out.println("Bad input: " + ex.getMessage());
        }
    }

    /**
     * It prints the slot numbers of parking lot having cars of same color.
     * If no car is found with this color, {@link com.callicoder.goparking.utils.MessageConstants#NOT_FOUND} is printed.
     * If parking lot is not created, {@link com.callicoder.goparking.utils.MessageConstants#PARKING_LOT_ALREADY_CREATED} is printed.
     *
     * @param color of the car.
     */
    public void getSlotNumbersByColor(String color) {
        try {
            if (!isParkingLotCreated()) {
                System.out.println(PARKING_LOT_NOT_CREATED);
                return;
            }
            List<Integer> slotsWithSameColorCars = parkingLot.getSlotNumbersByColor(color);
            if (slotsWithSameColorCars.isEmpty()) {
                System.out.println(NOT_FOUND);
            } else {
                String slotsWithSameColorCarsString = slotsWithSameColorCars
                        .stream()
                        .map(slot -> Integer.toString(slot))
                        .collect(Collectors.joining(", "));
                System.out.println(slotsWithSameColorCarsString);
            }
        } catch (IllegalArgumentException ex) {
            // Error log for developers can be printed here.
            System.out.println("Bad input: " + ex.getMessage());
        }
    }

    /**
     * It prints the slot number of a car with given registration number.
     * If no car is found with this color, {@link com.callicoder.goparking.utils.MessageConstants#NOT_FOUND} is printed.
     * If parking lot is not created, {@link com.callicoder.goparking.utils.MessageConstants#PARKING_LOT_ALREADY_CREATED} is printed.
     *
     * @param registrationNumber of the car.
     */
    public void getSlotNumberByRegistrationNumber(String registrationNumber) {
        Optional<Integer> slot = null;
        try {
            if (!isParkingLotCreated()) {
                System.out.println(PARKING_LOT_NOT_CREATED);
                return;
            }
            slot = parkingLot.getSlotNumberByRegistrationNumber(registrationNumber);
            if (!slot.isPresent()) {
                System.out.println(NOT_FOUND);
            } else {
                System.out.println(slot.get());
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("Bad input: " + ex.getMessage());
        } catch (RuntimeException e) {
            // Error log for developers can be printed here.
            System.out.println(INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Checks if parking lot exists or not.
     *
     * @return true if it exists else false.
     */
    private boolean isParkingLotCreated() {
        if (parkingLot == null) {
            return false;
        }
        return true;
    }
}
