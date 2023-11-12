package ru.clevertec.webservlet.repository;

import ru.clevertec.webservlet.model.UserWithRoles;
import ru.clevertec.webservlet.tables.pojos.User;

import java.util.Optional;

public interface UserRepository {

    Optional<UserWithRoles> findById(Long id);

    Optional<UserWithRoles> findByNicknameAndPassword(String nickname, String password);

    Optional<UserWithRoles> save(UserWithRoles userWithRoles);

    Optional<UserWithRoles> updateById(Long id, UserWithRoles userWithRoles);

    Optional<User> deleteById(Long id);

}
