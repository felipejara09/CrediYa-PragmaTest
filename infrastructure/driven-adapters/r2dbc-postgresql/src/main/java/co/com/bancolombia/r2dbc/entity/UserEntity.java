package co.com.bancolombia.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Table("users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEntity {

    @Id
    @Column("id_user")
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String identityNumber;
    private LocalDate dateBorn;
    private String address;
    private String phoneNumber;
    private Long idRol;
    private BigDecimal baseSalary;
}
