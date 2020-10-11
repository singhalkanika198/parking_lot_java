package com.callicoder.goparking.utils;

public interface MessageConstants {
    String PARKING_LOT_ALREADY_CREATED = "Oops! parking lot is already created.";
    String PARKING_LOT_NOT_CREATED = "Sorry, parking lot is not created yet.";

    String PARKING_LOT_CREATED_MSG = "Created a parking lot with %s slots";
    String PARKING_SLOT_ALLOCATED_MSG = "Allocated slot number: %s";
    String PARKING_SLOT_VACATED_MSG = "Vacated slot number: %s";
    String PARKING_LOT_FULL_MSG = "Sorry, parking lot is full";

    String SLOT_NO = "Slot No.";
    String REGISTRATION_NO = "Registration No";


    String Color = "Colour";
    String DUPLICATE_CAR_MESSAGE = "A car already parked with this registration number";
    String NOT_FOUND = "Not found";

    String INTERNAL_SERVER_ERROR = "No worries! Something went wrong. Kindly try again.";

    // Parking lot commands.
    String PARKING_LOT_HELP_COMMAND = "help";
    String PARKING_LOT_CREATE_COMMAND = "create_parking_lot";
    String PARKING_LOT_PARK_COMMAND = "park";
    String PARKING_LOT_STATUS_COMMAND = "status";
    String PARKING_LOT_LEAVE_COMMAND = "leave";
    String PARKING_LOT_REGISTRATION_NUMBERS_FOR_CARS_WITH_COLOUR_COMMAND = "registration_numbers_for_cars_with_colour";
    String PARKING_LOT_SLOT_NUMBERS_FOR_CARS_WITH_COLOUR_COMMAND = "slot_numbers_for_cars_with_colour";
    String PARKING_LOT_SLOT_NUMBER_FOR_REGISTRATION_NUMBER_COMMAND = "slot_number_for_registration_number";
}
