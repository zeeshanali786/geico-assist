package com.geico.claim.roadsideassistance.service.exception;

public class AssistantNotAvailableException extends RuntimeException {
    public AssistantNotAvailableException() {
        super("No available assistants found");
    }
}