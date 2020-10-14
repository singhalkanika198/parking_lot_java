package com.callicoder.goparking.interaction;

import com.callicoder.goparking.handler.ParkingLotCommandHandler;
import com.callicoder.goparking.exceptions.CommandNotFoundException;
import com.callicoder.goparking.exceptions.InvalidParameterException;
import com.callicoder.goparking.interaction.commands.*;

import java.util.HashMap;
import java.util.Map;

import static com.callicoder.goparking.utils.MessageConstants.*;

public class CommandFactory {
    private Map<String, Command> commands;

    private CommandFactory() {
        commands = new HashMap<>();
    }

    public static CommandFactory init(ParkingLotCommandHandler parkingLotCommandHandler) {
        final CommandFactory cf = new CommandFactory();

        cf.addCommand(PARKING_LOT_CREATE_COMMAND, new CreateLotCommand(parkingLotCommandHandler));
        cf.addCommand(PARKING_LOT_PARK_COMMAND, new ParkCommand(parkingLotCommandHandler));
        cf.addCommand(PARKING_LOT_STATUS_COMMAND, new StatusCommand(parkingLotCommandHandler));
        cf.addCommand(PARKING_LOT_LEAVE_COMMAND, new ParkingSpotLeaveCommand(parkingLotCommandHandler));
        cf.addCommand(PARKING_LOT_REGISTRATION_NUMBERS_FOR_CARS_WITH_COLOUR_COMMAND, new RegistrationNumbersForCarsWithColourCommand(parkingLotCommandHandler));
        cf.addCommand(PARKING_LOT_SLOT_NUMBERS_FOR_CARS_WITH_COLOUR_COMMAND, new SlotNumbersForCarsWithColourCommand(parkingLotCommandHandler));
        cf.addCommand(PARKING_LOT_SLOT_NUMBER_FOR_REGISTRATION_NUMBER_COMMAND, new SlotNumberForRegistrationNumberCommand(parkingLotCommandHandler));
        cf.addCommand(PARKING_LOT_PARK_WITH_TIME_COMMAND, new ParkWithTimeCommand(parkingLotCommandHandler));
        cf.addCommand(PARKING_LOT_LEAVE_WITH_TIME_COMMAND, new LeaveWithTimeCommand(parkingLotCommandHandler));
        return cf;
    }

    public void addCommand(String name, Command command) {
        commands.put(name, command);
    }

    public void executeCommand(String name, String[] params) throws CommandNotFoundException, InvalidParameterException {
        if (name.equalsIgnoreCase(PARKING_LOT_HELP_COMMAND)) {
            listCommandHelp();
            return;
        }
        if (commands.containsKey(name)) {
            commands.get(name).execute(params);
        } else {
            throw new CommandNotFoundException(name);
        }
    }

    public void listCommandHelp() {
        commands.keySet().stream()
                .map(command -> commands.get(command).helpText())
                .forEach(System.out::println);
    }

    public Map<String, Command> getCommands() {
        return commands;
    }
}
