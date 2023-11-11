package ru.clevertec.webservlet.repository;

import ru.clevertec.webservlet.dto.UserWithRoleIds;
import ru.clevertec.webservlet.dto.UserWithRoles;

import java.util.Optional;

public interface UserRepository {

    Optional<UserWithRoles> findById(Long id);

    Optional<UserWithRoles> save(UserWithRoleIds userWithRoleIds);

    Optional<UserWithRoles> updateById(Long id, UserWithRoleIds userWithRoleIds);

    Optional<UserWithRoles> deleteById(Long id);

}
