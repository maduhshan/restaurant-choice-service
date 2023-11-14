package gov.sg.tech.exception;

import gov.sg.tech.domain.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Global Exception handlers to generate API friendly responses based on exceptions
 * thrown throughout out the application journey
 *
 * @author Madushan
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @SendToUser("/topic/error")
    public ResponseEntity<ErrorResponse> handleInternalServerError(Exception e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(e.getMessage())
                        .build(), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(ConflictOperationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @SendToUser("/topic/error")
    public ResponseEntity<ErrorResponse> handleConflictError(ConflictOperationException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status(HttpStatus.CONFLICT.value())
                        .message(e.getMessage())
                        .build(), HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @SendToUser("/topic/error")
    public ResponseEntity<ErrorResponse> handleResourceNotFoundError(ResourceNotFoundException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message(e.getMessage())
                        .build(), HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @SendToUser("/topic/error")
    public ResponseEntity<ErrorResponse> handleBadRequestError(BadRequestException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(e.getMessage())
                        .build(), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @SendToUser("/topic/error")
    public ResponseEntity<ErrorResponse> handleConstraintViolationError(ConstraintViolationException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(e.getMessage())
                        .build(), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @SendToUser("/topic/error")
    public ResponseEntity<ErrorResponse> handleOperationNotAllowedError(UnsupportedOperationException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status(HttpStatus.FORBIDDEN.value())
                        .message(e.getMessage())
                        .build(), HttpStatus.FORBIDDEN
        );
    }
}
