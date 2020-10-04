package com.callicoder.goparking.interaction.commands;

import com.callicoder.goparking.exceptions.InvalidParameterException;
import com.callicoder.goparking.handler.ParkingLotCommandHandler;

public class SlotNumberForRegistrationNumberCommand implements Command {
    private ParkingLotCommandHandler parkingLotCommandHandler;

    public SlotNumberForRegistrationNumberCommand(ParkingLotCommandHandler parkingLotCommandHandler) {
        this.parkingLotCommandHandler = parkingLotCommandHandler;
    }

    @Override
    public String helpText() {
        return "slot_number_for_registration_number <car registration number>";
    }

    @Override
    public void execute(String[] params) throws InvalidParameterException {
        if (params.length != 1) {
            throw new InvalidParameterException("Expected one parameter <car registration number>");
        }
        parkingLotCommandHandler.getSlotNumberByRegistrationNumber(params[0]);
    }
}
