package co.com.bancolombia.r2dbc.mapper;


import org.mapstruct.Mapper;
import co.com.bancolombia.r2dbc.entity.UserEntity;
import co.com.bancolombia.model.user.User;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    UserEntity toEntity(User user);
    User toDomain(UserEntity entity);
}
