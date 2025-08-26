package co.com.bancolombia.api.dto;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;

public record UserDTO (Long id, String name, @NotBlank String lastName, String email,String identityNumber, LocalDate dateBorn, String address, String phoneNumber,String idRol, BigDecimal baseSalary){
}
