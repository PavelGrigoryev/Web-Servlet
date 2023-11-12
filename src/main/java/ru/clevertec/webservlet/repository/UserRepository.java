package ru.clevertec.webservlet.repository;

import ru.clevertec.webservlet.model.UserWithRoleIds;
import ru.clevertec.webservlet.model.UserWithRoles;
import ru.clevertec.webservlet.tables.pojos.User;

import java.util.Optional;

public interface UserRepository {

    Optional<UserWithRoles> findById(Long id);

    Optional<UserWithRoles> save(UserWithRoleIds userWithRoleIds);

    Optional<UserWithRoles> updateById(Long id, UserWithRoleIds userWithRoleIds);

    Optional<User> deleteById(Long id);

}
