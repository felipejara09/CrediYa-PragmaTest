package co.com.bancolombia.mysqlr2dbc;


import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserDataRepository repository;

    @Override
    public Mono<User> save(User user){
        UserData data = toData(user);
        return repository.save(data).map(this::toEntity);
    }

    @Override
    public Mono<User> findByCorreoElectronico(String correo){
        return repository.findByCorreoElectronico(correo).map(this::toEntity);
    }

    private UserData toData(User user){
        return UserData.builder()
                .id(user.getId())
                .nombres(user.getNombres())
                .apellidos(user.getApellidos())
                .correoElectronico(user.getCorreoElectronico())
                .fechaNacimiento(user.getFechaNacimiento())
                .direccion(user.getDireccion())
                .telefono(user.getTelefono())
                .salarioBase(user.getSalarioBase())
                .build();
    }

    private User toEntity(UserData data){
        return User.builder()
                .id(data.getId())
                .nombres(data.getNombres())
                .apellidos(data.getApellidos())
                .correoElectronico(data.getCorreoElectronico())
                .fechaNacimiento(data.getFechaNacimiento())
                .direccion(data.getDireccion())
                .telefono(data.getTelefono())
                .salarioBase(data.getSalarioBase())
                .build();
    }

}
