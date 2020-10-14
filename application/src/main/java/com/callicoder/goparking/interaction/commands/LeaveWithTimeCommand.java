package com.callicoder.goparking.interaction.commands;

import com.callicoder.goparking.exceptions.InvalidParameterException;
import com.callicoder.goparking.handler.ParkingLotCommandHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class LeaveWithTimeCommand implements Command {
    private ParkingLotCommandHandler parkingLotCommandHandler;

    public LeaveWithTimeCommand(ParkingLotCommandHandler parkingLotCommandHandler) {
        this.parkingLotCommandHandler = parkingLotCommandHandler;
    }

    @Override
    public String helpText() {
        return "leave_with_time <registrationNumber> <color> <time like: 2020-10-15T15:00>";
    }

    @Override
    public void execute(String[] params) throws InvalidParameterException {
        if (params.length < 3) {
            throw new InvalidParameterException("Expected three parameters <registrationNumber> <color> <time like: 2020-10-15T15:00>");
        }

        try {
            LocalDateTime leaveTime = LocalDateTime.parse(params[2]);
            parkingLotCommandHandler.leaveWithTime(params[0], params[1], leaveTime);
        } catch (DateTimeParseException e) {
            throw new InvalidParameterException("Expected third parameter in the form: 2020-10-15T15:00");
        }
    }
}
