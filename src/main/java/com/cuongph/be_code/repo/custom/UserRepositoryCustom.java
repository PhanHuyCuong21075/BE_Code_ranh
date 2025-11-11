package com.cuongph.be_code.repo.custom;

import com.cuongph.be_code.entity.UserEntity;

import java.util.List;

public interface UserRepositoryCustom {

    List<UserEntity> findAllBySuggestedFriends(Long username);
}
