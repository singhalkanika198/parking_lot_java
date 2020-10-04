package com.callicoder.goparking.interaction.command;

import com.callicoder.goparking.exceptions.InvalidParameterException;
import com.callicoder.goparking.handler.ParkingLotCommandHandler;
import com.callicoder.goparking.interaction.commands.RegistrationNumbersForCarsWithColourCommand;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegistrationNumbersForCarsWithColourCommandTests {
    private static ParkingLotCommandHandler parkingLotCommandHandler;
    private static RegistrationNumbersForCarsWithColourCommand registrationNumbersForCarsWithColourCommand;

    @BeforeAll
    public static void createCommand() {
        parkingLotCommandHandler = new ParkingLotCommandHandler();
        registrationNumbersForCarsWithColourCommand = new RegistrationNumbersForCarsWithColourCommand(parkingLotCommandHandler);
    }

    @Test
    public void executeWithNoArg_shouldThrowError() {
        String[] params = {};
        assertThrows(InvalidParameterException.class, () -> registrationNumbersForCarsWithColourCommand.execute(params));
    }

    @Test
    public void executeWithInvalidNumberOfArgs_shouldThrowError() {
        String[] params = {"blue", "black"};
        assertThrows(InvalidParameterException.class, () -> registrationNumbersForCarsWithColourCommand.execute(params));
    }

    @Test
    public void executeWithValidNumberOfArgs_shouldWork() {
        String[] params = {"blue"};
        assertDoesNotThrow(() -> registrationNumbersForCarsWithColourCommand.execute(params));
    }

    @Test
    public void testHelpText_success() {
        assertEquals(registrationNumbersForCarsWithColourCommand.helpText(), "registration_numbers_for_cars_with_colour <car color>");
    }
}
