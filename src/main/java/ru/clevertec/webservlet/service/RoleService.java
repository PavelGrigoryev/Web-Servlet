package ru.clevertec.webservlet.service;

import ru.clevertec.webservlet.dto.DeleteResponse;
import ru.clevertec.webservlet.dto.role.RoleResponse;
import ru.clevertec.webservlet.dto.role.RoleSaveRequest;
import ru.clevertec.webservlet.dto.role.RoleUpdateRequest;

import java.util.Set;

public interface RoleService {

    RoleResponse findById(Long id);

    Set<Long> findRoleIdsByIdIn(Set<Long> ids);

    RoleResponse save(RoleSaveRequest request);

    RoleResponse updateById(Long id, RoleUpdateRequest request);

    DeleteResponse deleteById(Long id);

}
