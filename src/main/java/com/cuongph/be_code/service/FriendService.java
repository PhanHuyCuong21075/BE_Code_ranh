package com.cuongph.be_code.service;

import com.cuongph.be_code.entity.User;

import java.util.List;

public interface FriendService {

    List<User> getFriends(String username);

    List<User> suggestFriends(String username);

}
