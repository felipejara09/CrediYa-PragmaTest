package co.com.bancolombia.r2dbc.mapper;

import org.mapstruct.Mapper;
import co.com.bancolombia.r2dbc.entity.UserEntity;
import co.com.bancolombia.model.user.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toEntity(User user);
    User toDomain(UserEntity entity);
}
