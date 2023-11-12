package ru.clevertec.webservlet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.webservlet.dto.user.UserResponse;
import ru.clevertec.webservlet.dto.user.UserSaveRequest;
import ru.clevertec.webservlet.dto.user.UserUpdateRequest;
import ru.clevertec.webservlet.model.UserWithRoleIds;

import java.time.LocalDateTime;

@Mapper(imports = LocalDateTime.class)
public interface UserMapper {


    @Mapping(target = "registerTime", expression = "java(LocalDateTime.now())")
    UserWithRoleIds fromSaveRequest(UserSaveRequest request);

    @Mapping(target = "roleIds", source = "request.roleIds")
    @Mapping(target = "password", source = "request.password")
    UserWithRoleIds mergeToUser(UserWithRoleIds userWithRoleIds, UserUpdateRequest request);

    UserResponse toResponse(UserWithRoleIds user);

}
