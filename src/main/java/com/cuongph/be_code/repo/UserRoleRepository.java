package com.cuongph.be_code.repo;

import com.cuongph.be_code.entity.UserRoleEntity;
import com.cuongph.be_code.repo.custom.UserRoleRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long>, UserRoleRepositoryCustom {
}