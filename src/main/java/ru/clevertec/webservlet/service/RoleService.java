package ru.clevertec.webservlet.service;

import java.util.Set;

public interface RoleService {

    Set<Long> findRoleIdsByIdIn(Set<Long> ids);

}
