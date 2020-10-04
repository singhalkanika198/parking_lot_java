package com.callicoder.goparking.exceptions;

public class SlotAlreadyLeftException extends RuntimeException {
    private Integer slotNumber;

    public SlotAlreadyLeftException(Integer slotNumber) {
        this.slotNumber = slotNumber;
    }

    @Override
    public String getMessage() {
        return "Slot number " + slotNumber + " was not occupied. Hence, can not be left.";
    }
}

