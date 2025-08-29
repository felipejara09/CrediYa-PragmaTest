package co.com.bancolombia.api.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateUserDTO (
        Long id,
        String name,
        String lastName,
        String email,
        String identityNumber,
        String phoneNumber,
        LocalDate dateBorn,
        String address,
        Long idRol,
        //@NotNull(message = "base Salary is requiered")
        //@DecimalMin(value = "0.0", inclusive = true, message = "base Salary must be >= 0")
       // @DecimalMax(value = "15000000.0", inclusive = true, message = "base Salary must be <= 15000000")
        BigDecimal baseSalary) {
}
