package ru.clevertec.webservlet.service.impl;

import ru.clevertec.webservlet.dto.DeleteResponse;
import ru.clevertec.webservlet.exception.NotFoundException;
import ru.clevertec.webservlet.exception.UniqueException;
import ru.clevertec.webservlet.dto.UserWithRoleIds;
import ru.clevertec.webservlet.dto.UserWithRoles;
import ru.clevertec.webservlet.repository.UserRepository;
import ru.clevertec.webservlet.repository.impl.UserRepositoryImpl;
import ru.clevertec.webservlet.service.UserService;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl() {
        userRepository = new UserRepositoryImpl();
    }

    @Override
    public UserWithRoles findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with ID " + id + " is not found"));
    }

    @Override
    public UserWithRoles save(UserWithRoleIds userWithRoleIds) {
        return userRepository.save(userWithRoleIds)
                .orElseThrow(() -> new UniqueException("User with username " + userWithRoleIds.getNickname()
                                                       + " is already exist"));
    }

    @Override
    public UserWithRoles updateById(Long id, UserWithRoleIds userWithRoleIds) {
        return userRepository.updateById(id, userWithRoleIds)
                .orElseThrow(() -> new NotFoundException("No User with ID " + id + " to update"));
    }

    @Override
    public DeleteResponse deleteById(Long id) {
        return userRepository.deleteById(id)
                .map(userWithRoles -> new DeleteResponse("User with ID " + id + " was successfully deleted"))
                .orElseThrow(() -> new NotFoundException("No User with ID " + id + " to delete"));
    }

}
