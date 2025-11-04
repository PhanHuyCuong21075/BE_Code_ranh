package com.cuongph.be_code.repo.custom.impl;

import com.cuongph.be_code.common.ws.SqlQueryUtil;
import com.cuongph.be_code.entity.FriendEntity;
import com.cuongph.be_code.entity.User;
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
    public Optional<FriendEntity> findRelation(Long user1, Long user2) {
        Map<String, Object> params = new HashMap<>();
        params.put("user1", user1);
        params.put("user2", user2);
        List<FriendEntity> result = sqlQueryUtil.queryForList(sqlFindRelation(), params, FriendEntity.class);
        return result.stream().findFirst();
    }

    @Override
    public List<User> findFriendsByUsername(String username) {
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        return sqlQueryUtil.queryForList(sqlFindFriendsByUsername(), params, User.class);
    }
}
