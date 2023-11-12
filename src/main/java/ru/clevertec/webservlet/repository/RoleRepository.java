package ru.clevertec.webservlet.repository;

import ru.clevertec.webservlet.tables.pojos.Role;

import java.util.Optional;
import java.util.Set;

public interface RoleRepository {

    Optional<Role> findById(Long id);

    Set<Long> findRoleIdsByIdIn(Set<Long> ids);

    Optional<Role> save(Role role);

    Optional<Role> updateById(Long id, Role role);

    Optional<Role> deleteById(Long id);

}
