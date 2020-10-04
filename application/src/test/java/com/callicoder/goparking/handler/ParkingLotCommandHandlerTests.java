package com.callicoder.goparking.handler;

import com.callicoder.goparking.domain.Car;
import com.callicoder.goparking.domain.ParkingSlot;
import com.callicoder.goparking.utils.StringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static com.callicoder.goparking.utils.MessageConstants.*;
import static org.junit.jupiter.api.Assertions.*;

public class ParkingLotCommandHandlerTests {
    private final String PRINTED_PARKING_LOT_CREATED_MSG_ONE_SLOT = String.format(PARKING_LOT_CREATED_MSG, 1);
    private final String PRINTED_PARKING_SLOT_ALLOCATED_MSG = PRINTED_PARKING_LOT_CREATED_MSG_ONE_SLOT + "\n" + String.format(PARKING_SLOT_ALLOCATED_MSG, 1);
    private final String PRINTED_PARKING_LOT_FULL_MSG = PRINTED_PARKING_SLOT_ALLOCATED_MSG + "\n" + PARKING_LOT_FULL_MSG;

    private static PrintStream sysOut;
    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeAll
    public static void setupStreams() {
        sysOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @BeforeEach
    public void resetStream() {
        outContent.reset();
    }

    @Test
    public void testCreateParkingLotOutput() {
        ParkingLotCommandHandler parkingLotCommandHandler = new ParkingLotCommandHandler();
        parkingLotCommandHandler.createParkingLot(10);

        assertEquals(String.format("Created a parking lot with 10 slots\n"), outContent.toString());
        assertEquals(String.format(PARKING_LOT_CREATED_MSG, 10) + "\n", outContent.toString());
    }

    @Test
    public void testCreateMultipleParkingLotOutput() {
        ParkingLotCommandHandler parkingLotCommandHandler = new ParkingLotCommandHandler();
        parkingLotCommandHandler.createParkingLot(10);
        parkingLotCommandHandler.createParkingLot(6);

        assertTrue(outContent.toString().endsWith(PARKING_LOT_ALREADY_CREATED + "\n"));
    }

    @Test
    public void testCreateMultipleParkingLotOutput_numSlotsEqualToZero_shouldPrintIllegalArgumentExceptionMessage() {
        ParkingLotCommandHandler parkingLotCommandHandler = new ParkingLotCommandHandler();
        parkingLotCommandHandler.createParkingLot(0);

        assertTrue(outContent.toString().startsWith("Bad input: "));
    }

    @Test
    public void testParkOutput() {
        ParkingLotCommandHandler parkingLotCommandHandler = new ParkingLotCommandHandler();
        parkingLotCommandHandler.createParkingLot(6);
        parkingLotCommandHandler.park("KA-01-HH-3141", "Black");

        assertTrue(outContent.toString().endsWith("Allocated slot number: 1\n"));
        assertEquals(String.format(PARKING_LOT_CREATED_MSG, 6) + "\n" +
                String.format(PARKING_SLOT_ALLOCATED_MSG, 1) + "\n", outContent.toString());
    }

    @Test
    public void testParkWithNoParkingLotOutput() {
        ParkingLotCommandHandler parkingLotCommandHandler = new ParkingLotCommandHandler();
        parkingLotCommandHandler.park("KA-01-HQ-4669", "White");
        assertTrue(outContent.toString().endsWith(PARKING_LOT_NOT_CREATED + "\n"));
    }

    @Test
    public void testParkWithDuplicateCar() {
        ParkingLotCommandHandler parkingLotCommandHandler = new ParkingLotCommandHandler();
        parkingLotCommandHandler.createParkingLot(6);
        assertTrue(outContent.toString().endsWith(String.format(PARKING_LOT_CREATED_MSG, 6) + "\n"));

        parkingLotCommandHandler.park("KA-01-HH-3141", "Black");
        assertTrue(outContent.toString().endsWith("Allocated slot number: 1\n"));

        parkingLotCommandHandler.park("KA-01-HH-3141", "White");
        assertTrue(outContent.toString().endsWith(DUPLICATE_CAR_MESSAGE + "\n"));
    }

    @Test
    public void testParkWithParkingLotAlreadyFull_shouldPrintParkingLotFullMessage() {
        ParkingLotCommandHandler parkingLotCommandHandler = new ParkingLotCommandHandler();
        parkingLotCommandHandler.createParkingLot(1);
        assertEquals(outContent.toString(), PRINTED_PARKING_LOT_CREATED_MSG_ONE_SLOT + "\n");

        parkingLotCommandHandler.park("KA-01-HH-3141", "Black");
        assertEquals(outContent.toString(), PRINTED_PARKING_SLOT_ALLOCATED_MSG + "\n");

        parkingLotCommandHandler.park("KA-01-HH-3142", "Black");
        assertEquals(outContent.toString(), PRINTED_PARKING_LOT_FULL_MSG + "\n");
    }

    @Test
    public void isCarAlreadyParked_carNotParkedEarlier_shouldReturnFalse() {
        ParkingLotCommandHandler parkingLotCommandHandler = new ParkingLotCommandHandler();
        parkingLotCommandHandler.createParkingLot(1);
        assertEquals(outContent.toString(), PRINTED_PARKING_LOT_CREATED_MSG_ONE_SLOT + "\n");

        parkingLotCommandHandler.park("KA-01-HH-3141", "Black");
        assertEquals(outContent.toString(), PRINTED_PARKING_SLOT_ALLOCATED_MSG + "\n");

        assertFalse(parkingLotCommandHandler.isCarAlreadyParked(new Car("MA-01-HH-3141", "Black")));
    }

    @Test
    public void isCarAlreadyParked_carParkedEarlier_shouldReturnTre() {
        ParkingLotCommandHandler parkingLotCommandHandler = new ParkingLotCommandHandler();
        parkingLotCommandHandler.createParkingLot(1);
        assertEquals(outContent.toString(), PRINTED_PARKING_LOT_CREATED_MSG_ONE_SLOT + "\n");

        parkingLotCommandHandler.park("KA-01-HH-3141", "Black");
        assertEquals(outContent.toString(), PRINTED_PARKING_SLOT_ALLOCATED_MSG + "\n");

        assertTrue(parkingLotCommandHandler.isCarAlreadyParked(new Car("KA-01-HH-3141", "Black")));
    }

    @Test
    public void testStatusWithNoParkingLotOutput() {
        ParkingLotCommandHandler parkingLotCommandHandler = new ParkingLotCommandHandler();
        parkingLotCommandHandler.status();
        assertTrue(outContent.toString().endsWith(PARKING_LOT_NOT_CREATED + "\n"));
    }

    @Test
    public void testStatus_shouldPrintParkingLotStatus() {
        ParkingLotCommandHandler parkingLotCommandHandler = new ParkingLotCommandHandler();
        parkingLotCommandHandler.createParkingLot(1);
        assertEquals(outContent.toString(), PRINTED_PARKING_LOT_CREATED_MSG_ONE_SLOT + "\n");

        parkingLotCommandHandler.park("KA-01-HH-3141", "Black");
        assertEquals(outContent.toString(), PRINTED_PARKING_SLOT_ALLOCATED_MSG + "\n");

        parkingLotCommandHandler.status();
        ParkingSlot parkingSlot = parkingLotCommandHandler.getParkingLot().getOccupiedSlots().stream().findFirst().get();
        assertEquals(outContent.toString(),
                PRINTED_PARKING_SLOT_ALLOCATED_MSG + "\n" + SLOT_NO + "    " + REGISTRATION_NO + "    " + Color + "\n" +
                        StringUtils.rightPadSpaces(Integer.toString(parkingSlot.getSlotNumber()), SLOT_NO.length()) +
                        "    " + StringUtils.rightPadSpaces(parkingSlot.getCar().getRegistrationNumber(), REGISTRATION_NO.length()) +
                        "    " + parkingSlot.getCar().getColor() + "\n"
        );
    }

    @Test
    public void testLeaveParkingSpot_success() {
        ParkingLotCommandHandler parkingLotCommandHandler = new ParkingLotCommandHandler();
        parkingLotCommandHandler.createParkingLot(1);
        assertEquals(outContent.toString(), PRINTED_PARKING_LOT_CREATED_MSG_ONE_SLOT + "\n");

        parkingLotCommandHandler.park("KA-01-HH-3141", "Black");
        assertEquals(outContent.toString(), PRINTED_PARKING_SLOT_ALLOCATED_MSG + "\n");

        parkingLotCommandHandler.leaveParkingSpot(1);
        assertEquals(outContent.toString(),
                PRINTED_PARKING_SLOT_ALLOCATED_MSG + "\n" + String.format(PARKING_SLOT_VACATED_MSG, 1) + "\n");
    }

    @Test
    public void testLeaveParkingSpot_toBeLeftSpotAlreadyLeft_shouldPrintSlotAlreadyLeftExceptionMessage() {
        ParkingLotCommandHandler parkingLotCommandHandler = new ParkingLotCommandHandler();
        parkingLotCommandHandler.createParkingLot(1);
        assertEquals(outContent.toString(), PRINTED_PARKING_LOT_CREATED_MSG_ONE_SLOT + "\n");

        parkingLotCommandHandler.leaveParkingSpot(1);
        assertTrue(outContent.toString().startsWith(PRINTED_PARKING_LOT_CREATED_MSG_ONE_SLOT + "\n" + "Bad input: "));
    }

    @Test
    public void testLeaveParkingSpot_toBeLeftSpotDoesNotExist_shouldPrintSlotNotFoundExceptionMessage() {
        ParkingLotCommandHandler parkingLotCommandHandler = new ParkingLotCommandHandler();
        parkingLotCommandHandler.createParkingLot(1);
        assertEquals(outContent.toString(), PRINTED_PARKING_LOT_CREATED_MSG_ONE_SLOT + "\n");

        parkingLotCommandHandler.leaveParkingSpot(100);
        assertTrue(outContent.toString().startsWith(PRINTED_PARKING_LOT_CREATED_MSG_ONE_SLOT + "\n" + "Bad input: "));
    }

    @Test
    public void testCarsRegNumbersWithSameColor_WithNoParkingLotOutput() {
        ParkingLotCommandHandler parkingLotCommandHandler = new ParkingLotCommandHandler();
        parkingLotCommandHandler.carsRegNumbersWithSameColor("White");
        assertTrue(outContent.toString().endsWith(PARKING_LOT_NOT_CREATED + "\n"));
    }

    @Test
    public void testCarsRegNumbersWithSameColor_WithNoCarsOfThisColorOutput() {
        ParkingLotCommandHandler parkingLotCommandHandler = new ParkingLotCommandHandler();
        parkingLotCommandHandler.createParkingLot(1);
        assertEquals(PRINTED_PARKING_LOT_CREATED_MSG_ONE_SLOT + "\n", outContent.toString());

        parkingLotCommandHandler.park("KA-01-HH-3141", "black");
        assertEquals(PRINTED_PARKING_SLOT_ALLOCATED_MSG + "\n", outContent.toString());

        parkingLotCommandHandler.carsRegNumbersWithSameColor("white");
        assertTrue(outContent.toString().startsWith(PRINTED_PARKING_SLOT_ALLOCATED_MSG + "\n" + NOT_FOUND + "\n"));
    }

    @Test
    public void testCarsRegNumbersWithSameColor_WithTwoCarsOfSameColorOutput() {
        ParkingLotCommandHandler parkingLotCommandHandler = new ParkingLotCommandHandler();
        parkingLotCommandHandler.createParkingLot(3);
        assertEquals(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n", outContent.toString());

        parkingLotCommandHandler.park("KA-01-HH-3141", "black");
        assertEquals(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n" + String.format(PARKING_SLOT_ALLOCATED_MSG, 1) + "\n", outContent.toString());

        parkingLotCommandHandler.park("KA-01-HH-3142", "white");
        assertEquals(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 1) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 2) + "\n",
                outContent.toString());

        parkingLotCommandHandler.park("KA-01-HH-3143", "white");
        assertEquals(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 1) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 2) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 3) + "\n",
                outContent.toString());

        parkingLotCommandHandler.carsRegNumbersWithSameColor("WHITE");
        assertEquals(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 1) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 2) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 3) + "\n" +
                        "KA-01-HH-3142, KA-01-HH-3143" + "\n",
                outContent.toString());
    }

    @Test
    public void testCarsRegNumbersWithSameColor_WithCarColorAsNull_shouldPrintIllegalArgumentExceptionMessage() {
        ParkingLotCommandHandler parkingLotCommandHandler = new ParkingLotCommandHandler();
        parkingLotCommandHandler.createParkingLot(3);
        assertEquals(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n", outContent.toString());

        parkingLotCommandHandler.park("KA-01-HH-3141", "black");
        assertEquals(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n" + String.format(PARKING_SLOT_ALLOCATED_MSG, 1) + "\n", outContent.toString());

        parkingLotCommandHandler.park("KA-01-HH-3142", "white");
        assertEquals(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 1) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 2) + "\n",
                outContent.toString());

        parkingLotCommandHandler.park("KA-01-HH-3143", "white");
        assertEquals(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 1) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 2) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 3) + "\n",
                outContent.toString());

        parkingLotCommandHandler.carsRegNumbersWithSameColor(null);
        assertTrue(outContent.toString().startsWith(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n" +
                String.format(PARKING_SLOT_ALLOCATED_MSG, 1) + "\n" +
                String.format(PARKING_SLOT_ALLOCATED_MSG, 2) + "\n" +
                String.format(PARKING_SLOT_ALLOCATED_MSG, 3) + "\n" +
                "Bad input: "));
    }

    @Test
    public void testGetSlotNumbersByColor_WithNoParkingLotOutput() {
        ParkingLotCommandHandler parkingLotCommandHandler = new ParkingLotCommandHandler();
        parkingLotCommandHandler.getSlotNumbersByColor("White");
        assertTrue(outContent.toString().endsWith(PARKING_LOT_NOT_CREATED + "\n"));
    }

    @Test
    public void testGetSlotNumbersByColor_WithNoCarsOfThisColorOutput() {
        ParkingLotCommandHandler parkingLotCommandHandler = new ParkingLotCommandHandler();
        parkingLotCommandHandler.createParkingLot(1);
        assertEquals(PRINTED_PARKING_LOT_CREATED_MSG_ONE_SLOT + "\n", outContent.toString());

        parkingLotCommandHandler.park("KA-01-HH-3141", "black");
        assertEquals(PRINTED_PARKING_SLOT_ALLOCATED_MSG + "\n", outContent.toString());

        parkingLotCommandHandler.getSlotNumbersByColor("white");
        assertTrue(outContent.toString().startsWith(PRINTED_PARKING_SLOT_ALLOCATED_MSG + "\n" + NOT_FOUND + "\n"));
    }

    @Test
    public void testGetSlotNumbersByColor_WithTwoCarsOfSameColorOutput() {
        ParkingLotCommandHandler parkingLotCommandHandler = new ParkingLotCommandHandler();
        parkingLotCommandHandler.createParkingLot(3);
        assertEquals(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n", outContent.toString());

        parkingLotCommandHandler.park("KA-01-HH-3141", "black");
        assertEquals(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n" + String.format(PARKING_SLOT_ALLOCATED_MSG, 1) + "\n", outContent.toString());

        parkingLotCommandHandler.park("KA-01-HH-3142", "white");
        assertEquals(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 1) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 2) + "\n",
                outContent.toString());

        parkingLotCommandHandler.park("KA-01-HH-3143", "white");
        assertEquals(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 1) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 2) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 3) + "\n",
                outContent.toString());

        parkingLotCommandHandler.getSlotNumbersByColor("WHITE");
        assertEquals(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 1) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 2) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 3) + "\n" +
                        "2, 3" + "\n",
                outContent.toString());
    }

    @Test
    public void testGetSlotNumbersByColor_WithCarColorAsNull_shouldPrintIllegalArgumentExceptionMessage() {
        ParkingLotCommandHandler parkingLotCommandHandler = new ParkingLotCommandHandler();
        parkingLotCommandHandler.createParkingLot(3);
        assertEquals(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n", outContent.toString());

        parkingLotCommandHandler.park("KA-01-HH-3141", "black");
        assertEquals(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n" + String.format(PARKING_SLOT_ALLOCATED_MSG, 1) + "\n", outContent.toString());

        parkingLotCommandHandler.park("KA-01-HH-3142", "white");
        assertEquals(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 1) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 2) + "\n",
                outContent.toString());

        parkingLotCommandHandler.park("KA-01-HH-3143", "white");
        assertEquals(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 1) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 2) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 3) + "\n",
                outContent.toString());

        parkingLotCommandHandler.getSlotNumbersByColor(null);
        assertTrue(outContent.toString().startsWith(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n" +
                String.format(PARKING_SLOT_ALLOCATED_MSG, 1) + "\n" +
                String.format(PARKING_SLOT_ALLOCATED_MSG, 2) + "\n" +
                String.format(PARKING_SLOT_ALLOCATED_MSG, 3) + "\n" +
                "Bad input: "));
    }

    @Test
    public void testGetSlotNumberByRegistrationNumber_WithNoParkingLotOutput() {
        ParkingLotCommandHandler parkingLotCommandHandler = new ParkingLotCommandHandler();
        parkingLotCommandHandler.getSlotNumberByRegistrationNumber("KA-01-HH-3143");
        assertTrue(outContent.toString().endsWith(PARKING_LOT_NOT_CREATED + "\n"));
    }

    @Test
    public void testGetSlotNumberByRegistrationNumber_WithNoCarWithGivenRegistrationNumber_shouldPrintNotFoundMessage() {
        ParkingLotCommandHandler parkingLotCommandHandler = new ParkingLotCommandHandler();
        parkingLotCommandHandler.createParkingLot(1);
        assertEquals(PRINTED_PARKING_LOT_CREATED_MSG_ONE_SLOT + "\n", outContent.toString());

        parkingLotCommandHandler.park("KA-01-HH-3141", "black");
        assertEquals(PRINTED_PARKING_SLOT_ALLOCATED_MSG + "\n", outContent.toString());

        parkingLotCommandHandler.getSlotNumberByRegistrationNumber("KA-01-HH-3143");
        assertTrue(outContent.toString().startsWith(PRINTED_PARKING_SLOT_ALLOCATED_MSG + "\n" + NOT_FOUND + "\n"));
    }

    @Test
    public void testGetSlotNumberByRegistrationNumber_shouldReturnTheSlotOParkedCar() {
        ParkingLotCommandHandler parkingLotCommandHandler = new ParkingLotCommandHandler();
        parkingLotCommandHandler.createParkingLot(3);
        assertEquals(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n", outContent.toString());

        parkingLotCommandHandler.park("KA-01-HH-3141", "black");
        assertEquals(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n" + String.format(PARKING_SLOT_ALLOCATED_MSG, 1) + "\n", outContent.toString());

        parkingLotCommandHandler.park("KA-01-HH-3142", "white");
        assertEquals(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 1) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 2) + "\n",
                outContent.toString());

        parkingLotCommandHandler.park("KA-01-HH-3143", "white");
        assertEquals(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 1) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 2) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 3) + "\n",
                outContent.toString());

        parkingLotCommandHandler.getSlotNumberByRegistrationNumber("KA-01-HH-3143");
        assertEquals(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 1) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 2) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 3) + "\n" +
                        "3" + "\n",
                outContent.toString());
    }

    @Test
    public void testGetSlotNumberByRegistrationNumber_WithCarRegNumberAsNull_shouldPrintIllegalArgumentExceptionMessage() {
        ParkingLotCommandHandler parkingLotCommandHandler = new ParkingLotCommandHandler();
        parkingLotCommandHandler.createParkingLot(3);
        assertEquals(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n", outContent.toString());

        parkingLotCommandHandler.park("KA-01-HH-3141", "black");
        assertEquals(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n" + String.format(PARKING_SLOT_ALLOCATED_MSG, 1) + "\n", outContent.toString());

        parkingLotCommandHandler.park("KA-01-HH-3142", "white");
        assertEquals(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 1) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 2) + "\n",
                outContent.toString());

        parkingLotCommandHandler.park("KA-01-HH-3143", "white");
        assertEquals(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 1) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 2) + "\n" +
                        String.format(PARKING_SLOT_ALLOCATED_MSG, 3) + "\n",
                outContent.toString());

        parkingLotCommandHandler.getSlotNumberByRegistrationNumber(null);
        assertTrue(outContent.toString().startsWith(String.format(PARKING_LOT_CREATED_MSG, 3) + "\n" +
                String.format(PARKING_SLOT_ALLOCATED_MSG, 1) + "\n" +
                String.format(PARKING_SLOT_ALLOCATED_MSG, 2) + "\n" +
                String.format(PARKING_SLOT_ALLOCATED_MSG, 3) + "\n" +
                "Bad input: "));
    }

    @AfterAll
    public static void revertStreams() {
        System.setOut(sysOut);
    }
}
