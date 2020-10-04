package com.callicoder.goparking.interaction.commands;

import com.callicoder.goparking.exceptions.InvalidParameterException;
import com.callicoder.goparking.handler.ParkingLotCommandHandler;

/**
 * Functionality to return the registration number of all cars {@link com.callicoder.goparking.domain.Car}
 * with same car.
 */
public class RegistrationNumbersForCarsWithColourCommand implements Command {
    private ParkingLotCommandHandler parkingLotCommandHandler;

    public RegistrationNumbersForCarsWithColourCommand(ParkingLotCommandHandler parkingLotCommandHandler) {
        this.parkingLotCommandHandler = parkingLotCommandHandler;
    }

    @Override
    public String helpText() {
        return "registration_numbers_for_cars_with_colour <car color>";
    }

    @Override
    public void execute(String[] params) throws InvalidParameterException {
        if (params.length != 1) {
            throw new InvalidParameterException("Expected one parameter <car color>");
        }
        parkingLotCommandHandler.carsRegNumbersWithSameColor(params[0]);
    }
}


