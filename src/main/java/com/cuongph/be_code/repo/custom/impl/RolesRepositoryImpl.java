package com.cuongph.be_code.repo.custom.impl;

import com.cuongph.be_code.common.ws.SqlQueryUtil;
import com.cuongph.be_code.repo.custom.RolesRepositoryCustom;
import com.cuongph.be_code.repo.custom.query.RolesQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class RolesRepositoryImpl extends RolesQuery implements RolesRepositoryCustom {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private SqlQueryUtil sqlQueryUtil;

    @Override
    public List<String> getRoleCodeByUserId(Long userId) {
        Query query = entityManager.createNativeQuery(sqlRoleByUserName());
        query.setParameter("userId", userId);

        return query.getResultList();
    }
}
