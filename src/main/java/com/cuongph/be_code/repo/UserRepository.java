package com.cuongph.be_code.repo;

import com.cuongph.be_code.entity.UserEntity;
import com.cuongph.be_code.repo.custom.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
public interface UserRepository extends JpaRepository<UserEntity, Long> , UserRepositoryCustom {
    Optional<UserEntity> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
