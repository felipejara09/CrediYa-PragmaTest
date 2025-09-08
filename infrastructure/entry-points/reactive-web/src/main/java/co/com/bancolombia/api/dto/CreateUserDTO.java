package co.com.bancolombia.api.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateUserDTO (
        String name,
        String lastName,
        String email,
        String identityNumber,
        String phoneNumber,
        LocalDate dateBorn,
        String address,
        Long idRole,
<<<<<<< HEAD
        BigDecimal baseSalary,
        String password) {
=======
        BigDecimal baseSalary) {
>>>>>>> e9327b0a8449ea6154e02b3317113961689f247a
}
