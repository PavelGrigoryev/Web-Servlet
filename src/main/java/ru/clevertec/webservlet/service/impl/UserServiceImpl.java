package ru.clevertec.webservlet.service.impl;

import io.jsonwebtoken.JwtException;
import org.mapstruct.factory.Mappers;
import ru.clevertec.webservlet.dto.DeleteResponse;
import ru.clevertec.webservlet.dto.user.AuthorizationResponse;
import ru.clevertec.webservlet.dto.user.LoginRequest;
import ru.clevertec.webservlet.dto.user.UserResponse;
import ru.clevertec.webservlet.dto.user.UserSaveRequest;
import ru.clevertec.webservlet.dto.user.UserUpdateRequest;
import ru.clevertec.webservlet.exception.NotFoundException;
import ru.clevertec.webservlet.exception.UniqueException;
import ru.clevertec.webservlet.mapper.UserMapper;
import ru.clevertec.webservlet.model.UserWithRoles;
import ru.clevertec.webservlet.repository.UserRepository;
import ru.clevertec.webservlet.repository.impl.UserRepositoryImpl;
import ru.clevertec.webservlet.service.JwtService;
import ru.clevertec.webservlet.service.RoleService;
import ru.clevertec.webservlet.service.UserService;
import ru.clevertec.webservlet.tables.pojos.Role;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public UserServiceImpl() {
        userRepository = new UserRepositoryImpl();
        roleService = new RoleServiceImpl();
        jwtService = new JwtServiceImpl();
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Override
    public UserResponse findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("User with ID " + id + " is not found"));
    }

    @Override
    public UserWithRoles findByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(() -> new JwtException("Wrong Jwt! User with nickname " + nickname + " was deleted by ADMIN"));
    }

    @Override
    public AuthorizationResponse findByNicknameAndPassword(LoginRequest request) {
        return userRepository.findByNicknameAndPassword(request.nickname(), request.password())
                .map(user -> {
                    String jwt = jwtService.generateToken(user);
                    return userMapper.toAuthResponse(user, jwt, jwtService.extractExpiration(jwt));
                })
                .orElseThrow(() -> new NotFoundException("User with nickname " + request.nickname()
                                                         + " and with password " + request.password() + " is not found"));
    }

    @Override
    public UserResponse save(UserSaveRequest request) {
        checkForRoleExistence(request.roleIds());
        return Optional.of(request)
                .map(userMapper::fromSaveRequest)
                .flatMap(userRepository::save)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new UniqueException("User with username " + request.nickname() + " is already exist"));
    }

    @Override
    public AuthorizationResponse register(UserSaveRequest request) {
        checkForRoleExistence(request.roleIds());
        return Optional.of(request)
                .map(userMapper::fromSaveRequest)
                .flatMap(userRepository::save)
                .map(user -> {
                    String jwt = jwtService.generateToken(user);
                    return userMapper.toAuthResponse(user, jwt, jwtService.extractExpiration(jwt));
                })
                .orElseThrow(() -> new UniqueException("User with username " + request.nickname() + " is already exist"));
    }

    @Override
    public UserResponse updateById(Long id, UserUpdateRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    checkForRoleExistence(request.roleIds());
                    request.roleIds().removeAll(user.getRoles()
                            .stream()
                            .map(Role::getId)
                            .collect(Collectors.toSet()));
                    return userMapper.mergeToUser(user, request);
                })
                .flatMap(user -> userRepository.updateById(id, user))
                .map(userMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("No User with ID " + id + " to update"));
    }

    @Override
    public DeleteResponse deleteById(Long id) {
        return userRepository.deleteById(id)
                .map(userWithRoles -> new DeleteResponse("User with ID " + id + " was successfully deleted"))
                .orElseThrow(() -> new NotFoundException("No User with ID " + id + " to delete"));
    }

    private void checkForRoleExistence(Set<Long> roleIds) {
        Set<Long> roleIdsByIdIn = roleService.findRoleIdsByIdIn(roleIds);
        Set<Long> notFoundRoles = roleIds.stream()
                .filter(roleId -> !roleIdsByIdIn.contains(roleId))
                .collect(Collectors.toSet());
        if (!notFoundRoles.isEmpty()) {
            throw new NotFoundException("Roles with ID in " + notFoundRoles + " is not found");
        }
    }

}
