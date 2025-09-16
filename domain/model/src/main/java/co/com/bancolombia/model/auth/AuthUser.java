package co.com.bancolombia.model.auth;


import lombok.*;



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
