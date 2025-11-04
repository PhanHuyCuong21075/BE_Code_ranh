package com.cuongph.be_code.repo.custom;

import com.cuongph.be_code.entity.FriendEntity;
import com.cuongph.be_code.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface FriendRepositoryCustom {
    List<FriendEntity> findAcceptedFriends(Long userId);

    Optional<FriendEntity> findRelation(Long user1, Long user2);

    List<UserEntity> findFriendsByUsername(String username);
}
