package co.com.bancolombia.usecase.user.exception;

public class InvalidEmailFormatException extends DomainValidationException {
    public InvalidEmailFormatException(String email) {
        super("Invalid email format: " + email);
    }
}
