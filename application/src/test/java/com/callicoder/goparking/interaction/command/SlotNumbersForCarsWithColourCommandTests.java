package com.callicoder.goparking.interaction.command;

import com.callicoder.goparking.exceptions.InvalidParameterException;
import com.callicoder.goparking.handler.ParkingLotCommandHandler;
import com.callicoder.goparking.interaction.commands.SlotNumbersForCarsWithColourCommand;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SlotNumbersForCarsWithColourCommandTests {
    private static ParkingLotCommandHandler parkingLotCommandHandler;
    private static SlotNumbersForCarsWithColourCommand slotNumbersForCarsWithColourCommand;

    @BeforeAll
    public static void createCommand() {
        parkingLotCommandHandler = new ParkingLotCommandHandler();
        slotNumbersForCarsWithColourCommand = new SlotNumbersForCarsWithColourCommand(parkingLotCommandHandler);
    }

    @Test
    public void executeWithNoArg_shouldThrowError() {
        String[] params = {};
        assertThrows(InvalidParameterException.class, () -> slotNumbersForCarsWithColourCommand.execute(params));
    }

    @Test
    public void executeWithValidArgs_shouldWork() {
        String[] params = {"blue"};
        assertDoesNotThrow(() -> slotNumbersForCarsWithColourCommand.execute(params));
    }

    @Test
    public void executeWithInvalidNumberOfArgs_shouldThrowError() {
        String[] params = {"white", "black"};
        assertThrows(InvalidParameterException.class, () -> slotNumbersForCarsWithColourCommand.execute(params));
    }

    @Test
    public void testHelpText_success() {
        assertEquals(slotNumbersForCarsWithColourCommand.helpText(), "slot_numbers_for_cars_with_colour <car color>");
    }
}
