package com.callicoder.goparking.exceptions;

import static com.callicoder.goparking.utils.MessageConstants.PARKING_LOT_HELP_COMMAND;

public class CommandNotFoundException extends Exception {
    private String name;

    public CommandNotFoundException(String name) {
        this.name = name;
    }

    @Override
    public String getMessage() {
        return name + " is not a valid command. See: " + PARKING_LOT_HELP_COMMAND;
    }
}
