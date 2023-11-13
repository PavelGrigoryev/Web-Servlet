package ru.clevertec.webservlet.service;

import ru.clevertec.webservlet.dto.DeleteResponse;
import ru.clevertec.webservlet.dto.user.AuthorizationResponse;
import ru.clevertec.webservlet.dto.user.LoginRequest;
import ru.clevertec.webservlet.dto.user.UserResponse;
import ru.clevertec.webservlet.dto.user.UserSaveRequest;
import ru.clevertec.webservlet.dto.user.UserUpdateRequest;
import ru.clevertec.webservlet.model.UserWithRoles;

public interface UserService {

    UserResponse findById(Long id);

    UserWithRoles findByNickname(String nickname);

    AuthorizationResponse findByNicknameAndPassword(LoginRequest request);

    UserResponse save(UserSaveRequest request);

    AuthorizationResponse register(UserSaveRequest request);

    UserResponse updateById(Long id, UserUpdateRequest request);

    DeleteResponse deleteById(Long id);

}
