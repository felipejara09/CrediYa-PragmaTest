package co.com.bancolombia.usecase.user.exception;

public class RequiredFieldMissingException extends DomainValidationException {
    public RequiredFieldMissingException(String field) {
        super("Field '" + field + "' is required and cannot be blank");
    }
}
