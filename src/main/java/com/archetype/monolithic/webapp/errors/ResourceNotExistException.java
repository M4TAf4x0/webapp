package com.archetype.monolithic.webapp.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception for nonexistent elements.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotExistException extends RuntimeException {

    public ResourceNotExistException() {
        super("Resource not exists exception");
    }

    public ResourceNotExistException(String message) {
        super(message);
    }
}