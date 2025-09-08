package co.com.bancolombia.model.user;
import lombok.*;
//import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

        private Long id;
        private String name;
        private String lastName;
        private String email;
        private String identityNumber;
        private LocalDate dateBorn;
        private String address;
        private String phoneNumber;
        private Long idRole;
        private BigDecimal baseSalary;
        private String password;


}
