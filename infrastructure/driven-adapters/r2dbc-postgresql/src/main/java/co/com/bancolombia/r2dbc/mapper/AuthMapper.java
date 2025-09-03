package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.auth.AuthUser;
import co.com.bancolombia.r2dbc.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface  AuthMapper {
     AuthUser toAuth(UserEntity entity);
}
