package ru.clevertec.webservlet.service.impl;

import org.mapstruct.factory.Mappers;
import ru.clevertec.webservlet.dto.DeleteResponse;
import ru.clevertec.webservlet.dto.role.RoleResponse;
import ru.clevertec.webservlet.dto.role.RoleSaveRequest;
import ru.clevertec.webservlet.dto.role.RoleUpdateRequest;
import ru.clevertec.webservlet.exception.NotFoundException;
import ru.clevertec.webservlet.exception.UniqueException;
import ru.clevertec.webservlet.mapper.RoleMapper;
import ru.clevertec.webservlet.repository.RoleRepository;
import ru.clevertec.webservlet.repository.impl.RoleRepositoryImpl;
import ru.clevertec.webservlet.service.RoleService;

import java.util.Optional;
import java.util.Set;

public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImpl() {
        roleRepository = new RoleRepositoryImpl();
        roleMapper = Mappers.getMapper(RoleMapper.class);
    }

    @Override
    public RoleResponse findById(Long id) {
        return roleRepository.findById(id)
                .map(roleMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Role with ID " + id + " is not found"));
    }

    @Override
    public Set<Long> findRoleIdsByIdIn(Set<Long> ids) {
        return roleRepository.findRoleIdsByIdIn(ids);
    }

    @Override
    public RoleResponse save(RoleSaveRequest request) {
        return Optional.of(request)
                .map(roleMapper::fromSaveRequest)
                .flatMap(roleRepository::save)
                .map(roleMapper::toResponse)
                .orElseThrow(() -> new UniqueException("Role with name " + request.name() + " is already exist"));
    }

    @Override
    public RoleResponse updateById(Long id, RoleUpdateRequest request) {
        return Optional.of(request)
                .map(roleMapper::fromUpdateRequest)
                .flatMap(role -> roleRepository.updateById(id, role))
                .map(roleMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("No Role with ID " + id + " to update"));
    }

    @Override
    public DeleteResponse deleteById(Long id) {
        return roleRepository.deleteById(id)
                .map(role -> new DeleteResponse("Role with ID " + id + " was successfully deleted"))
                .orElseThrow(() -> new NotFoundException("No Role with ID " + id + " to delete"));
    }

}
