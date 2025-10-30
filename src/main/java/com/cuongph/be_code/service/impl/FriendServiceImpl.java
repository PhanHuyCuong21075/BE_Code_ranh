package com.cuongph.be_code.service.impl;

import com.cuongph.be_code.entity.Friend;
import com.cuongph.be_code.entity.User;
import com.cuongph.be_code.repo.FriendRepository;
import com.cuongph.be_code.repo.UserRepository;
import com.cuongph.be_code.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepo;
    private final UserRepository userRepo;

    public List<User> getFriends(String username) {
        return userRepo.findByUsername(username)
                .map(user -> {
                    Long userId = user.getId();
                    List<Friend> relations = friendRepo.findAcceptedFriends(userId);

                    return relations.stream()
                            .map(f -> f.getRequester().getId().equals(userId)
                                    ? f.getReceiver()
                                    : f.getRequester())
                            .toList();
                })
                .orElse(List.of());
    }

    @Override
    public List<User> suggestFriends(String username) {
        User currentUser = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));

        // 1️⃣ Lấy danh sách bạn bè hiện tại
        List<User> currentFriends = getFriends(username);

        // 2️⃣ Lấy tất cả bạn của bạn bè (friend-of-friend)
        Set<User> friendOfFriends = new HashSet<>();
        for (User friend : currentFriends) {
            List<User> fof = getFriends(friend.getUsername());
            friendOfFriends.addAll(fof);
        }

        // Loại bỏ chính mình và bạn bè hiện tại
        friendOfFriends.remove(currentUser);
        currentFriends.forEach(friendOfFriends::remove);

        // 3️⃣ Nếu có bạn chung → ưu tiên họ
        if (!friendOfFriends.isEmpty()) {
            return new ArrayList<>(friendOfFriends);
        }

        // 4️⃣ Nếu không có bạn chung → random user khác
        List<User> allUsers = userRepo.findAll();
        allUsers.remove(currentUser);
        allUsers.removeAll(currentFriends);

        Collections.shuffle(allUsers);
        return allUsers.stream().limit(5).collect(Collectors.toList());
    }

}
