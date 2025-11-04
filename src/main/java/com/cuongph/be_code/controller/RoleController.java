package com.cuongph.be_code.controller;

import com.cuongph.be_code.dto.request.GetPostRequest;
import com.cuongph.be_code.dto.response.ResponseData;
import com.cuongph.be_code.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/list")
    public ResponseData<Object> getAllRoles() {
        return new ResponseData<>().success(roleService.getAllRoles());
    }
}
