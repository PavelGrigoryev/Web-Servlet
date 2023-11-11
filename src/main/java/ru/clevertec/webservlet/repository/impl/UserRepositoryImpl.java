package ru.clevertec.webservlet.repository.impl;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import ru.clevertec.webservlet.dto.UserWithRoleIds;
import ru.clevertec.webservlet.dto.UserWithRoles;
import ru.clevertec.webservlet.repository.UserRepository;
import ru.clevertec.webservlet.tables.pojos.User;
import ru.clevertec.webservlet.util.HikariConnectionManager;

import java.util.Optional;

import static org.jooq.impl.DSL.arrayAgg;
import static ru.clevertec.webservlet.Tables.ROLE;
import static ru.clevertec.webservlet.Tables.USER;
import static ru.clevertec.webservlet.Tables.USER_ROLES;

public class UserRepositoryImpl implements UserRepository {

    private final DSLContext dslContext;

    public UserRepositoryImpl() {
        dslContext = DSL.using(HikariConnectionManager.getConnection());
    }

    @Override
    public Optional<UserWithRoles> findById(Long id) {
        return dslContext.select(USER.ID,
                        USER.NICKNAME,
                        USER.PASSWORD,
                        USER.REGISTER_TIME,
                        arrayAgg(ROLE.NAME).as("roles"))
                .from(USER)
                .join(USER_ROLES).on(USER.ID.eq(USER_ROLES.USER_ID))
                .join(ROLE).on(USER_ROLES.ROLE_ID.eq(ROLE.ID))
                .where(USER.ID.eq(id))
                .groupBy(USER.ID)
                .fetchOptional()
                .map(r -> r.into(UserWithRoles.class));
    }

    @Override
    public Optional<UserWithRoles> save(UserWithRoleIds userWithRoleIds) {
        return dslContext.insertInto(USER)
                .set(USER.NICKNAME, userWithRoleIds.getNickname())
                .set(USER.PASSWORD, userWithRoleIds.getPassword())
                .set(USER.REGISTER_TIME, userWithRoleIds.getRegisterTime())
                .onDuplicateKeyIgnore()
                .returning()
                .fetchOptional()
                .map(userRecord -> userRecord.into(User.class))
                .map(user -> insertIntoUserRoles(userWithRoleIds, user))
                .flatMap(u -> findById(u.getId()));
    }

    @Override
    public Optional<UserWithRoles> updateById(Long id, UserWithRoleIds userWithRoleIds) {
        return dslContext.update(USER)
                .set(USER.PASSWORD, userWithRoleIds.getPassword())
                .where(USER.ID.eq(id))
                .returning()
                .fetchOptional()
                .map(userRecord -> userRecord.into(User.class))
                .map(user -> insertIntoUserRoles(userWithRoleIds, user))
                .flatMap(u -> findById(u.getId()));
    }

    @Override
    public Optional<UserWithRoles> deleteById(Long id) {
        return dslContext.deleteFrom(USER)
                .where(USER.ID.eq(id))
                .returning()
                .fetchOptional()
                .map(userRecord -> userRecord.into(User.class))
                .flatMap(u -> findById(u.getId()));
    }

    private User insertIntoUserRoles(UserWithRoleIds userWithRoleIds, User user) {
        userWithRoleIds.getRoleIds()
                .forEach(roleId -> dslContext.insertInto(USER_ROLES)
                        .set(USER_ROLES.USER_ID, user.getId())
                        .set(USER_ROLES.ROLE_ID, roleId)
                        .execute());
        return user;
    }

}
