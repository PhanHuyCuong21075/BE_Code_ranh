package com.cuongph.be_code.service.impl;

import com.cuongph.be_code.entity.UserEntity;
import com.cuongph.be_code.repo.UserRepository;
import com.cuongph.be_code.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService {
    @Autowired
    private UserRepository repo;

    @Override
    public Optional<UserEntity> getByUserName(String userName) {
        return repo.findByUsername(userName);
    }
}
