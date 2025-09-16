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
<<<<<<< HEAD
        Long idRole,
=======
        Long idRol,
>>>>>>> e9327b0a8449ea6154e02b3317113961689f247a
        BigDecimal baseSalary,
        String password
) {
}
