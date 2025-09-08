package co.com.bancolombia.api.dto;
import java.math.BigDecimal;
import java.time.LocalDate;

public record UserDTO (Long id, String name, String lastName, String email,String identityNumber, LocalDate dateBorn, String address, String phoneNumber,Long idRole, BigDecimal baseSalary, String password){
}
