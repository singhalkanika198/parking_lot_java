package com.callicoder.goparking.interaction.command;

import com.callicoder.goparking.exceptions.InvalidParameterException;
import com.callicoder.goparking.handler.ParkingLotCommandHandler;
import com.callicoder.goparking.interaction.commands.ParkingSpotLeaveCommand;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParkingSpotLeaveCommandTests {
    private static ParkingLotCommandHandler parkingLotCommandHandler;
    private static ParkingSpotLeaveCommand parkingSpotLeaveCommand;

    @BeforeAll
    public static void createCommand() {
        parkingLotCommandHandler = new ParkingLotCommandHandler();
        parkingSpotLeaveCommand = new ParkingSpotLeaveCommand(parkingLotCommandHandler);
    }

    @Test
    public void executeWithNoArg_shouldThrowError() {
        String[] params = {};
        assertThrows(InvalidParameterException.class, () -> parkingSpotLeaveCommand.execute(params));
    }

    @Test
    public void executeWithInvalidArgs_shouldThrowError() {
        String[] params = {"slot_number_string"};
        assertThrows(InvalidParameterException.class, () -> parkingSpotLeaveCommand.execute(params));
    }

    @Test
    public void executeWithInvalidNumberOfArgs_shouldThrowError() {
        String[] params = {"2", "3"};
        assertThrows(InvalidParameterException.class, () -> parkingSpotLeaveCommand.execute(params));
    }

    @Test
    public void executeWithValidNumberOfArgs_shouldWork() {
        String[] params = {"4"};
        assertDoesNotThrow(() -> parkingSpotLeaveCommand.execute(params));
    }

    @Test
    public void testHelpText_success() {
        assertEquals(parkingSpotLeaveCommand.helpText(), "leave <slotNo>");
    }
}
