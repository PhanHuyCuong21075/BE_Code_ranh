package com.cuongph.be_code.repo;

import com.cuongph.be_code.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
}