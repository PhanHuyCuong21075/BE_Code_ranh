package com.cuongph.be_code.repo.custom.impl;

import com.cuongph.be_code.common.ws.SqlQueryUtil;
import com.cuongph.be_code.entity.UserEntity;
import com.cuongph.be_code.repo.custom.UserRepositoryCustom;
import com.cuongph.be_code.repo.custom.query.UserRepositoryQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl extends UserRepositoryQuery implements UserRepositoryCustom {
    @Autowired
    private SqlQueryUtil sqlQueryUtil;


    @Override
    public List<UserEntity> findAllBySuggestedFriends(Long userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        return sqlQueryUtil.queryForList(sqlSuggestedFriends(), params, UserEntity.class);
    }
}
