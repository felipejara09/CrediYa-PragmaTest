package co.com.bancolombia.usecase.user.exception;

import java.math.BigDecimal;

public class InvalidBaseSalaryRangeException extends DomainValidationException {
    public InvalidBaseSalaryRangeException(BigDecimal value) {
        super("baseSalary must be between 0 and 15000000. Provided: " + value);
    }
}
