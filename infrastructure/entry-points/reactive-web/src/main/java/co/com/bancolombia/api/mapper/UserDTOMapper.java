package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.dto.CreateUserDTO;
import co.com.bancolombia.api.dto.UserDTO;
import co.com.bancolombia.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {
    @Mapping(target = "id", ignore = true)
    User toDomain(CreateUserDTO createUserDTO);
    UserDTO toResponse(User user);
}
