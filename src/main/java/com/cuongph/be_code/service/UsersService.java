package com.cuongph.be_code.service;

import com.cuongph.be_code.entity.User;

import java.util.Optional;

public interface UsersService {
    Optional<User> getByUserName(String userName);

}
