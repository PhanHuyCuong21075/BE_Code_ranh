package com.cuongph.be_code.repo.custom;

import com.cuongph.be_code.entity.UserRoleEntity;

import java.util.List;

public interface UserRoleRepositoryCustom {

    List<UserRoleEntity> findByUserId(Long userId);

}
