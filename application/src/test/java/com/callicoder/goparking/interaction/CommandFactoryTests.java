package com.callicoder.goparking.interaction;

import com.callicoder.goparking.exceptions.CommandNotFoundException;
import com.callicoder.goparking.exceptions.InvalidParameterException;
import com.callicoder.goparking.handler.ParkingLotCommandHandler;
import com.callicoder.goparking.interaction.commands.Command;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommandFactoryTests {

    @Test
    public void init_shouldInitializeAllCommands() {
        ParkingLotCommandHandler parkingLotCommandHandler = new ParkingLotCommandHandler();
        CommandFactory commandFactory = CommandFactory.init(parkingLotCommandHandler);


        assertTrue(commandFactory.getCommands().keySet().contains("create_parking_lot"));
        assertTrue(commandFactory.getCommands().keySet().contains("park"));
        assertTrue(commandFactory.getCommands().keySet().contains("status"));
    }

    @Test
    public void executeInvalidCommand_shouldThrowError() {
        ParkingLotCommandHandler parkingLotCommandHandler = new ParkingLotCommandHandler();
        CommandFactory commandFactory = CommandFactory.init(parkingLotCommandHandler);

        String[] params = {"random"};
        assertThrows(CommandNotFoundException.class, () -> commandFactory.executeCommand("alpha", params));
    }

    @Test
    public void executeValidCommand_shouldExecuteAValidCommand() throws CommandNotFoundException, InvalidParameterException {
        ParkingLotCommandHandler parkingLotCommandHandler = new ParkingLotCommandHandler();
        CommandFactory commandFactory = CommandFactory.init(parkingLotCommandHandler);

        String[] params = {"random"};
        commandFactory.addCommand("alpha", new Command() {
            @Override
            public String helpText() {
                return null;
            }

            @Override
            public void execute(String[] strings) {

            }
        });
        commandFactory.executeCommand("alpha", params);
    }
}
