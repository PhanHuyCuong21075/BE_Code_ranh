package com.cuongph.be_code.repo.custom.impl;

import com.cuongph.be_code.common.ws.SqlQueryUtil;
import com.cuongph.be_code.entity.FriendEntity;
import com.cuongph.be_code.entity.UserEntity;
import com.cuongph.be_code.repo.custom.FriendRepositoryCustom;
import com.cuongph.be_code.repo.custom.query.FriendRepositoryQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class FriendRepositoryImpl extends FriendRepositoryQuery implements FriendRepositoryCustom {

    @Autowired
    private SqlQueryUtil sqlQueryUtil;

    @Override
    public List<FriendEntity> findAcceptedFriends(Long userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        return sqlQueryUtil.queryForList(sqlFindAcceptedFriends(), params, FriendEntity.class);
    }

    @Override
    public Optional<FriendEntity> findFriendship(Long userId1, Long userId2) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId1", userId1);
        params.put("userId2", userId2);

        List<FriendEntity> results = sqlQueryUtil.queryForList(
                sqlFindFriendship(),
                params,
                FriendEntity.class
        );

        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}