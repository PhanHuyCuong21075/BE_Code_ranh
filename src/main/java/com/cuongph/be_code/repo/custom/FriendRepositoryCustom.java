package com.cuongph.be_code.repo.custom;

import com.cuongph.be_code.entity.Friend;
import com.cuongph.be_code.entity.User;

import java.util.List;
import java.util.Optional;

public interface FriendRepositoryCustom {
    List<Friend> findAcceptedFriends(Long userId);

    Optional<Friend> findRelation(Long user1, Long user2);

    List<User> findFriendsByUsername(String username);
}
