package com.cuongph.be_code.service.impl;

import com.cuongph.be_code.repo.RolesRepository;
import com.cuongph.be_code.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RolesRepository rolesRepository;

    @Override
    public Object getAllRoles() {
        return rolesRepository.findAll();
    }
}
