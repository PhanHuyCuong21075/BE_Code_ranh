package com.cuongph.be_code.service;

import com.cuongph.be_code.entity.UserEntity;

import java.util.Optional;

public interface UsersService {
    Optional<UserEntity> getByUserName(String userName);

}
