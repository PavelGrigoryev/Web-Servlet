package ru.clevertec.webservlet.repository;

import ru.clevertec.webservlet.tables.pojos.Role;

import java.util.Optional;

public interface RoleRepository {

    Optional<Role> findById(Long id);

    Optional<Role> save(Role role);

    Optional<Role> update(Role role);

    Optional<Role> deleteById(Long id);

}
