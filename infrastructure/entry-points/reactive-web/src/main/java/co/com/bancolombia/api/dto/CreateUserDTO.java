package co.com.bancolombia.api.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateUserDTO (
        Long id,
        @NotBlank(message = "Name is required") String name,
        @NotBlank(message = "LastName is required") String lastName,
        @NotBlank(message = "email is required")
        @Email(
                message = "Please enter a valid email address",
                regexp  = "^[A-Za-z0-9._%+-]+@(?:[A-Za-z0-9-]+\\.)+[A-Za-z]{2,63}$"
        )String email,
        String identityNumber,
        String phoneNumber,
        LocalDate dateBorn,
        String address,
        Long idRol,
        @NotNull(message = "base Salary is requiered")
        @DecimalMin(value = "0.0", inclusive = true, message = "base Salary must be >= 0")
        @DecimalMax(value = "15000000.0", inclusive = true, message = "base Salary must be <= 15000000")
        BigDecimal baseSalary) {
}
