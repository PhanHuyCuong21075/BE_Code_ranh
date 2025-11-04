package com.cuongph.be_code.controller;

import com.cuongph.be_code.dto.response.ResponseData;
import com.cuongph.be_code.entity.RoleEntity;
import com.cuongph.be_code.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public ResponseData<Object> getAllRoles() {
        List<RoleEntity> roles = roleService.getAllRoles();
        return new ResponseData<>().success(roles);
    }

}
