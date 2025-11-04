package com.cuongph.be_code.repo;

import com.cuongph.be_code.entity.RoleEntity;
import com.cuongph.be_code.repo.custom.RolesRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface RolesRepository extends JpaRepository<RoleEntity, Long>, RolesRepositoryCustom {
    List<String> getRoleCodeByUserName(String userName);

    Optional<RoleEntity> findByCode(String code);
}
