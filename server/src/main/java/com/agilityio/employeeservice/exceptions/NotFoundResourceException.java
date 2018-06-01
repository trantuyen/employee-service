package com.agilityio.employeeservice.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Handle resource not found exception
 * TODO:: Move to a base exception class
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
public class NotFoundResourceException extends RuntimeException {

    public NotFoundResourceException(String message) {
        super(message);
    }
}
