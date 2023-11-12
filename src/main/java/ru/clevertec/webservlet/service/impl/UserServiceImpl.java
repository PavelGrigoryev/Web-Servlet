package ru.clevertec.webservlet.service.impl;

import org.mapstruct.factory.Mappers;
import ru.clevertec.webservlet.dto.DeleteResponse;
import ru.clevertec.webservlet.dto.user.UserResponse;
import ru.clevertec.webservlet.dto.user.UserSaveRequest;
import ru.clevertec.webservlet.dto.user.UserUpdateRequest;
import ru.clevertec.webservlet.exception.NotFoundException;
import ru.clevertec.webservlet.exception.UniqueException;
import ru.clevertec.webservlet.mapper.UserMapper;
import ru.clevertec.webservlet.repository.UserRepository;
import ru.clevertec.webservlet.repository.impl.UserRepositoryImpl;
import ru.clevertec.webservlet.service.UserService;

import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl() {
        userRepository = new UserRepositoryImpl();
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Override
    public UserResponse findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("User with ID " + id + " is not found"));
    }

    @Override
    public UserResponse save(UserSaveRequest request) {
        return Optional.of(request)
                .map(userMapper::fromSaveRequest)
                .flatMap(userRepository::save)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new UniqueException("User with username " + request.nickname() + " is already exist"));
    }

    @Override
    public UserResponse updateById(Long id, UserUpdateRequest request) {
        return Optional.of(request)
                .map(userMapper::fromUpdateRequest)
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

}
