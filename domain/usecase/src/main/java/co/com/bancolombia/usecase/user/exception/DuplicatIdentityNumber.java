package co.com.bancolombia.usecase.user.exception;

public class DuplicatIdentityNumber extends DomainValidationException {
    public DuplicatIdentityNumber(String identityNumber) {
        super("Identification number = " + identityNumber + " already exists ");
    }
}
