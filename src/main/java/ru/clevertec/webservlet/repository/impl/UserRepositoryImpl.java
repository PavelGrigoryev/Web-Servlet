package ru.clevertec.webservlet.repository.impl;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import ru.clevertec.webservlet.model.UserWithRoles;
import ru.clevertec.webservlet.repository.UserRepository;
import ru.clevertec.webservlet.tables.pojos.Role;
import ru.clevertec.webservlet.tables.pojos.User;
import ru.clevertec.webservlet.util.HikariConnectionManager;

import java.util.Optional;

import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.select;
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
        return getUserWithRoles(USER.ID.eq(id));
    }

    @Override
    public Optional<UserWithRoles> findByNickname(String nickname) {
        return getUserWithRoles(USER.NICKNAME.eq(nickname));
    }

    @Override
    public Optional<UserWithRoles> findByNicknameAndPassword(String nickname, String password) {
        return getUserWithRoles(USER.NICKNAME.eq(nickname).and(USER.PASSWORD.eq(password)));
    }

    @Override
    public Optional<UserWithRoles> save(UserWithRoles userWithRoles) {
        return dslContext.insertInto(USER)
                .set(USER.NICKNAME, userWithRoles.getNickname())
                .set(USER.PASSWORD, userWithRoles.getPassword())
                .set(USER.REGISTER_TIME, userWithRoles.getRegisterTime())
                .onDuplicateKeyIgnore()
                .returning()
                .fetchOptional()
                .map(userRecord -> userRecord.into(User.class))
                .map(user -> insertIntoUserRoles(userWithRoles, user))
                .flatMap(u -> findById(u.getId()));
    }

    @Override
    public Optional<UserWithRoles> updateById(Long id, UserWithRoles userWithRoles) {
        return dslContext.update(USER)
                .set(USER.PASSWORD, userWithRoles.getPassword())
                .where(USER.ID.eq(id))
                .returning()
                .fetchOptional()
                .map(userRecord -> userRecord.into(User.class))
                .map(user -> insertIntoUserRoles(userWithRoles, user))
                .flatMap(u -> findById(u.getId()));
    }

    @Override
    public Optional<User> deleteById(Long id) {
        deleteFromUserRoles(id);
        return dslContext.deleteFrom(USER)
                .where(USER.ID.eq(id))
                .returning()
                .fetchOptional()
                .map(userRecord -> userRecord.into(User.class));
    }

    private Optional<UserWithRoles> getUserWithRoles(Condition condition) {
        return dslContext.select(
                        USER.ID,
                        USER.NICKNAME,
                        USER.PASSWORD,
                        USER.REGISTER_TIME,
                        multiset(select(ROLE)
                                .from(USER_ROLES)
                                .join(ROLE).on(USER_ROLES.ROLE_ID.eq(ROLE.ID))
                                .where(USER_ROLES.USER_ID.eq(USER.ID)))
                                .convertFrom(r -> r.into(Role.class)).as("roles"))
                .from(USER)
                .where(condition)
                .fetchOptional()
                .map(r -> r.into(UserWithRoles.class));
    }

    private User insertIntoUserRoles(UserWithRoles userWithRoles, User user) {
        userWithRoles.getRoles()
                .stream()
                .map(Role::getId)
                .forEach(roleId -> dslContext.insertInto(USER_ROLES)
                        .set(USER_ROLES.USER_ID, user.getId())
                        .set(USER_ROLES.ROLE_ID, roleId)
                        .execute());
        return user;
    }

    private void deleteFromUserRoles(Long id) {
        dslContext.deleteFrom(USER_ROLES)
                .where(USER_ROLES.USER_ID.eq(id))
                .execute();
    }

}
