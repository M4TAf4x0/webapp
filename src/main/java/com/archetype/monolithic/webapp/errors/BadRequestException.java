package com.archetype.monolithic.webapp.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception for bad requests parameters.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    public BadRequestException() {
        super("Bad request exception");
    }

    public BadRequestException(String message) {
        super(message);
    }
}