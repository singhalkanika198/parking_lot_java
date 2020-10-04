package com.callicoder.goparking.interaction.command;

import com.callicoder.goparking.exceptions.InvalidParameterException;
import com.callicoder.goparking.handler.ParkingLotCommandHandler;
import com.callicoder.goparking.interaction.commands.SlotNumberForRegistrationNumberCommand;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SlotNumberForRegistrationNumberCommandTests {
    private static ParkingLotCommandHandler parkingLotCommandHandler;
    private static SlotNumberForRegistrationNumberCommand slotNumberForRegistrationNumberCommand;

    @BeforeAll
    public static void createCommand() {
        parkingLotCommandHandler = new ParkingLotCommandHandler();
        slotNumberForRegistrationNumberCommand = new SlotNumberForRegistrationNumberCommand(parkingLotCommandHandler);
    }

    @Test
    public void executeWithNoArg_shouldThrowError() {
        String[] params = {};
        assertThrows(InvalidParameterException.class, () -> slotNumberForRegistrationNumberCommand.execute(params));
    }

    @Test
    public void executeWithInvalidNumberOfArgs_shouldWork() {
        String[] params = {"KA-01-HQ-4669", "KA-01-HQ-4662"};
        assertThrows(InvalidParameterException.class, () -> slotNumberForRegistrationNumberCommand.execute(params));
    }

    @Test
    public void executeWithValidNumberOfArgs_shouldWork() {
        String[] params = {"KA-01-HQ-4669"};
        assertDoesNotThrow(() -> slotNumberForRegistrationNumberCommand.execute(params));
    }

    @Test
    public void testHelpText_success() {
        assertEquals(slotNumberForRegistrationNumberCommand.helpText(), "slot_number_for_registration_number <car registration number>");
    }
}
