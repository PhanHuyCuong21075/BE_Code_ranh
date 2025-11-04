package com.cuongph.be_code.repo;

import com.cuongph.be_code.entity.UserDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetailEntity, Long> {
    UserDetailEntity findByUserName(String userName);
}
