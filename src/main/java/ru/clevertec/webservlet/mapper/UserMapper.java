package ru.clevertec.webservlet.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.clevertec.webservlet.dto.user.AuthorizationResponse;
import ru.clevertec.webservlet.dto.user.UserResponse;
import ru.clevertec.webservlet.dto.user.UserSaveRequest;
import ru.clevertec.webservlet.dto.user.UserUpdateRequest;
import ru.clevertec.webservlet.model.UserWithRoles;
import ru.clevertec.webservlet.tables.pojos.Role;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Mapper
public interface UserMapper {

    @Mapping(target = "registerTime", expression = "java(LocalDateTime.now())")
    @Mapping(target = "roles", source = "roleIds")
    UserWithRoles fromSaveRequest(UserSaveRequest request);

    @Mapping(target = "roles", source = "request.roleIds")
    @Mapping(target = "password", source = "request.password")
    UserWithRoles mergeToUser(UserWithRoles userWithRoles, UserUpdateRequest request);

    UserResponse toResponse(UserWithRoles user);

    AuthorizationResponse toAuthResponse(UserWithRoles user, String jwt, LocalDateTime jwtExpiration);

    @IterableMapping(qualifiedByName = "idToRole")
    List<Role> idsToRoles(Set<Long> ids);

    @Named("idToRole")
    default Role idToRole(Long id) {
        return new Role(id, null, null);
    }

}
