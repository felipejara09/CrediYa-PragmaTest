package co.com.bancolombia.model.auth;


import lombok.*;

import java.util.Set;

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
