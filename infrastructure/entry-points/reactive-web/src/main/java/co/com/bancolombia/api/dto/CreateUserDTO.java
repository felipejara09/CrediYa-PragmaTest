package co.com.bancolombia.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateUserDTO (String name, String lastName, String email, String phoneNumber, LocalDate dateBorn, String address, BigDecimal baseSalary) {
}
