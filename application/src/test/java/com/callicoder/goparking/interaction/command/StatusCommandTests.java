package com.callicoder.goparking.interaction.command;

import com.callicoder.goparking.handler.ParkingLotCommandHandler;
import com.callicoder.goparking.interaction.commands.StatusCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusCommandTests {
    private static ParkingLotCommandHandler parkingLotCommandHandler;
    private static StatusCommand statusCommand;

    @BeforeAll
    public static void createCommand() {
        parkingLotCommandHandler = new ParkingLotCommandHandler();
        statusCommand = new StatusCommand(parkingLotCommandHandler);
    }

    @Test
    public void execute_shouldWork() {
        Assertions.assertDoesNotThrow(() -> statusCommand.execute(null));
    }

    @Test
    public void testHelpText_success() {
        assertEquals(statusCommand.helpText(), "status");
    }
}
