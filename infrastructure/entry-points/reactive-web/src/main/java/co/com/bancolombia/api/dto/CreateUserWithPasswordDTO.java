package co.com.bancolombia.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateUserWithPasswordDTO(
        String name,
        String lastName,
        String email,
        String identityNumber,
        String phoneNumber,
        LocalDate dateBorn,
        String address,
        Long idRole,
        BigDecimal baseSalary,
        String password
) {
}
