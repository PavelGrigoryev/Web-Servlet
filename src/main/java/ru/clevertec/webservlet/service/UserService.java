package ru.clevertec.webservlet.service;

import ru.clevertec.webservlet.dto.DeleteResponse;
import ru.clevertec.webservlet.dto.UserWithRoleIds;
import ru.clevertec.webservlet.dto.UserWithRoles;

public interface UserService {

    UserWithRoles findById(Long id);

    UserWithRoles save(UserWithRoleIds userWithRoleIds);

    UserWithRoles updateById(Long id, UserWithRoleIds userWithRoleIds);

    DeleteResponse deleteById(Long id);

}
