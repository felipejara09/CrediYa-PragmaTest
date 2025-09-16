package co.com.bancolombia.model.auth;


import lombok.*;

<<<<<<< HEAD
=======
import java.util.Set;
>>>>>>> 64ae74362c4ef6e5c96ad00eb9499a158b0963b0

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthUser {
    private Long id;
    private String email;
    private String password;
    private Long idRole;
}
