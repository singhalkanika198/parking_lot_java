package com.callicoder.goparking.interaction.commands;

import com.callicoder.goparking.exceptions.InvalidParameterException;
import com.callicoder.goparking.handler.ParkingLotCommandHandler;
import com.callicoder.goparking.utils.StringUtils;

public class ParkingSpotLeaveCommand implements Command {
    private ParkingLotCommandHandler parkingLotCommandHandler;

    public ParkingSpotLeaveCommand(ParkingLotCommandHandler parkingLotCommandHandler) {
        this.parkingLotCommandHandler = parkingLotCommandHandler;
    }

    @Override
    public String helpText() {
        return "leave <slotNo>";
    }

    @Override
    public void execute(String[] params) throws InvalidParameterException {
        if (params.length != 1) {
            throw new InvalidParameterException("Expected one parameter <slotNo>");
        }

        if (!StringUtils.isInteger(params[0])) {
            throw new InvalidParameterException("numSlots must be an integer");
        }

        parkingLotCommandHandler.leaveParkingSpot(Integer.parseInt(params[0]));
    }

}
