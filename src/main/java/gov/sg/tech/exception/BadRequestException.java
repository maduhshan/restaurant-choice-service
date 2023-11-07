package gov.sg.tech.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException (String message) {
        super(message);
    }
}
