package ru.clevertec.webservlet.service.impl;

import ru.clevertec.webservlet.repository.RoleRepository;
import ru.clevertec.webservlet.repository.impl.RoleRepositoryImpl;
import ru.clevertec.webservlet.service.RoleService;

import java.util.Set;

public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl() {
        roleRepository = new RoleRepositoryImpl();
    }

    @Override
    public Set<Long> findRoleIdsByIdIn(Set<Long> ids) {
        return roleRepository.findRoleIdsByIdIn(ids);
    }

}
