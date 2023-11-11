package gov.sg.tech.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception to serve HTTP 403 FORBIDDEN Type errors
 *
 * @author Madushan
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class OperationNotAllowedException extends RuntimeException {

    public OperationNotAllowedException(String message) {
        super(message);
    }
}
