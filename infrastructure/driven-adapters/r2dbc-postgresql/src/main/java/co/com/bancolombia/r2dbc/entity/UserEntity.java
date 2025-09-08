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
    @Column("last_name")
    private String lastName;
    private String email;
    @Column("identity_number")
    private String identityNumber;
    private LocalDate dateBorn;
    private String address;
    @Column("phone_number")
    private String phoneNumber;
<<<<<<< HEAD
    @Column("id_role")
    private Long idRole;
    @Column("base_salary")
=======
    private Long idRole;
>>>>>>> e9327b0a8449ea6154e02b3317113961689f247a
    private BigDecimal baseSalary;
    private String password;
}
