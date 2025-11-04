package com.cuongph.be_code.repo.custom.impl;

import com.cuongph.be_code.common.ws.SqlQueryUtil;
import com.cuongph.be_code.entity.PostEntity;
import com.cuongph.be_code.repo.custom.PostRepositoryCustom;
import com.cuongph.be_code.repo.custom.query.PostRepositoryQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PostRepositoryImpl extends PostRepositoryQuery implements PostRepositoryCustom {

    @Autowired
    private SqlQueryUtil sqlQueryUtil;

    @Override
    public List<PostEntity> findPostsByUserIds(List<Long> userIds, Long userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userIds", userIds);
        params.put("userId", userId);
        return sqlQueryUtil.queryForList(sqlFindPostsByUserIds(), params, PostEntity.class);
    }

    @Override
    public List<PostEntity> findAllPublicPostsAndUser(Long userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        return sqlQueryUtil.queryForList(sqlFindAllPublicPostsAndUser(), params, PostEntity.class);
    }
}
