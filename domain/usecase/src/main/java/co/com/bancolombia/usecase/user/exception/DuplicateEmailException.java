package co.com.bancolombia.usecase.user.exception;

public class DuplicateEmailException extends DomainValidationException {
  public DuplicateEmailException(String email){
    super(email );
  }
}
