package cl.bch.cloud.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Custom exception for handling too many requests.
 */
public class TooManyRequestException extends ResponseStatusException {

    public TooManyRequestException(Throwable t) {
        this("Too many requests", t);
    }

    public TooManyRequestException(String message, Throwable t) {
        super(HttpStatus.TOO_MANY_REQUESTS, message, t);
    }

    public TooManyRequestException(String message) {
        super(HttpStatus.TOO_MANY_REQUESTS, message);
    }
}
