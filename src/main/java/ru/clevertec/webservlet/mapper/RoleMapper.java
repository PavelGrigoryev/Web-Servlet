package ru.clevertec.webservlet.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.webservlet.dto.role.RoleResponse;
import ru.clevertec.webservlet.dto.role.RoleSaveRequest;
import ru.clevertec.webservlet.dto.role.RoleUpdateRequest;
import ru.clevertec.webservlet.tables.pojos.Role;

@Mapper
public interface RoleMapper {

    Role fromSaveRequest(RoleSaveRequest  request);

    Role fromUpdateRequest(RoleUpdateRequest request);

    RoleResponse toResponse(Role role);

}
