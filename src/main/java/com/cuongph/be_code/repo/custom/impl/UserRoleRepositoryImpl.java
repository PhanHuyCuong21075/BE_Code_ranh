package com.cuongph.be_code.repo.custom.impl;

import com.cuongph.be_code.entity.UserRoleEntity;
import com.cuongph.be_code.repo.custom.UserRoleRepositoryCustom;
import com.cuongph.be_code.repo.custom.query.UserRoleRepositoryQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class UserRoleRepositoryImpl extends UserRoleRepositoryQuery implements UserRoleRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List findByUserId(Long userId) {
        return entityManager.createNativeQuery(sqlFindByUserId(), UserRoleEntity.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
