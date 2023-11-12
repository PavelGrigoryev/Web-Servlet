package ru.clevertec.webservlet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.webservlet.dto.user.UserResponse;
import ru.clevertec.webservlet.dto.user.UserSaveRequest;
import ru.clevertec.webservlet.dto.user.UserUpdateRequest;
import ru.clevertec.webservlet.model.UserWithRoleIds;
import ru.clevertec.webservlet.model.UserWithRoles;

import java.time.LocalDateTime;

@Mapper(imports = LocalDateTime.class)
public interface UserMapper {


    @Mapping(target = "registerTime", expression = "java(LocalDateTime.now())")
    UserWithRoleIds fromSaveRequest(UserSaveRequest request);

    UserWithRoleIds fromUpdateRequest(UserUpdateRequest request);

    UserResponse toResponse(UserWithRoles user);

}
