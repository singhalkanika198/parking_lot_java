package com.callicoder.goparking.interaction.commands;

import com.callicoder.goparking.handler.ParkingLotCommandHandler;

public class StatusCommand implements Command {
    private ParkingLotCommandHandler parkingLotCommandHandler;

    public StatusCommand(ParkingLotCommandHandler parkingLotCommandHandler) {
        this.parkingLotCommandHandler = parkingLotCommandHandler;
    }

    @Override
    public String helpText() {
        return "status";
    }

    @Override
    public void execute(String[] params) {
        this.parkingLotCommandHandler.status();
    }
}
