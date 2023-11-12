package ru.clevertec.webservlet.repository;

import ru.clevertec.webservlet.model.UserWithRoleIds;
import ru.clevertec.webservlet.tables.pojos.User;

import java.util.Optional;

public interface UserRepository {

    Optional<UserWithRoleIds> findById(Long id);

    Optional<UserWithRoleIds> save(UserWithRoleIds userWithRoleIds);

    Optional<UserWithRoleIds> updateById(Long id, UserWithRoleIds userWithRoleIds);

    Optional<User> deleteById(Long id);

}
