package ru.clevertec.webservlet.service;

import ru.clevertec.webservlet.dto.DeleteResponse;
import ru.clevertec.webservlet.dto.user.LoginRequest;
import ru.clevertec.webservlet.dto.user.UserResponse;
import ru.clevertec.webservlet.dto.user.UserSaveRequest;
import ru.clevertec.webservlet.dto.user.UserUpdateRequest;

public interface UserService {

    UserResponse findById(Long id);

    UserResponse findByNicknameAndPassword(LoginRequest request);

    UserResponse save(UserSaveRequest request);

    UserResponse updateById(Long id, UserUpdateRequest request);

    DeleteResponse deleteById(Long id);

}
