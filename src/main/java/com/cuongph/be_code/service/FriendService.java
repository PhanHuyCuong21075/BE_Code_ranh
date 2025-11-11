package com.cuongph.be_code.service;

import com.cuongph.be_code.dto.response.PendingFriendResponse;
import com.cuongph.be_code.entity.UserEntity;

import java.util.List;

public interface FriendService {

    List<UserEntity> getFriends(String username);

    List<UserEntity> suggestFriends(String username);

    String processRequestFriend(Long receiverId);

    List<PendingFriendResponse> getPendingRequest(String username);
}
