package ru.clevertec.webservlet.repository.impl;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import ru.clevertec.webservlet.repository.RoleRepository;
import ru.clevertec.webservlet.tables.pojos.Role;
import ru.clevertec.webservlet.util.HikariConnectionManager;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.clevertec.webservlet.Tables.ROLE;
import static ru.clevertec.webservlet.Tables.USER_ROLES;

public class RoleRepositoryImpl implements RoleRepository {

    private final DSLContext dslContext;

    public RoleRepositoryImpl() {
        dslContext = DSL.using(HikariConnectionManager.getConnection());
    }

    @Override
    public Optional<Role> findById(Long id) {
        return dslContext.fetchOptional(ROLE, ROLE.ID.eq(id))
                .map(roleRecord -> roleRecord.into(Role.class));
    }

    @Override
    public Set<Long> findRoleIdsByIdIn(Set<Long> ids) {
        return dslContext.select(ROLE.ID)
                .from(ROLE)
                .where(ROLE.ID.in(ids))
                .fetch()
                .stream()
                .map(r -> r.get(ROLE.ID))
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<Role> save(Role role) {
        return dslContext.insertInto(ROLE)
                .set(ROLE.NAME, role.getName())
                .onDuplicateKeyIgnore()
                .returning()
                .fetchOptional()
                .map(roleRecord -> roleRecord.into(Role.class));
    }

    @Override
    public Optional<Role> update(Role role) {
        return dslContext.update(ROLE)
                .set(ROLE.DESCRIPTION, role.getDescription())
                .where(ROLE.ID.eq(role.getId()))
                .returning()
                .fetchOptional()
                .map(roleRecord -> roleRecord.into(Role.class));
    }

    @Override
    public Optional<Role> deleteById(Long id) {
        deleteFromUserRoles(id);
        return dslContext.deleteFrom(ROLE)
                .where(ROLE.ID.eq(id))
                .returning()
                .fetchOptional()
                .map(roleRecord -> roleRecord.into(Role.class));
    }

    private void deleteFromUserRoles(Long id) {
        dslContext.deleteFrom(USER_ROLES)
                .where(USER_ROLES.ROLE_ID.eq(id))
                .execute();
    }

}
