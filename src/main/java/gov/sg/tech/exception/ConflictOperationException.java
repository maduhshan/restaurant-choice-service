package gov.sg.tech.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception to serve HTTP 409 CONFLICT Type errors
 *
 * @author Madushan
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class ConflictOperationException extends RuntimeException {

    public ConflictOperationException(String message) {
        super(message);
    }
}
