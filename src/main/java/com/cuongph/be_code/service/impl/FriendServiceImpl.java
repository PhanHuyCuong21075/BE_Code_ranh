package com.cuongph.be_code.service.impl;

import com.cuongph.be_code.entity.FriendEntity;
import com.cuongph.be_code.entity.UserEntity;
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
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepo;
    private final UserRepository userRepo;

    public List<UserEntity> getFriends(String username) {
        // 1️⃣ Tìm người dùng theo username
        return userRepo.findByUsername(username)
                .map(user -> {
                    Long userId = user.getId();

                    // 2️⃣ Lấy tất cả các quan hệ bạn bè đã được chấp nhận
                    List<FriendEntity> acceptedFriends = friendRepo.findAcceptedFriends(userId);

                    // 3️⃣ Duyệt qua từng quan hệ, xác định ID của người bạn
                    List<Long> friendIds = acceptedFriends.stream()
                            .map(f -> f.getRequesterId().equals(userId)
                                    ? f.getReceiverId()
                                    : f.getRequesterId())
                            .toList();

                    // 4️⃣ Lấy danh sách UserEntity tương ứng từ ID
                    return userRepo.findAllById(friendIds);
                })
                // 5️⃣ Nếu không tìm thấy user → trả về list rỗng
                .orElse(List.of());
    }


    @Override
    public List<UserEntity> suggestFriends(String username) {
        UserEntity currentUserEntity = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));

        // 1️⃣ Lấy danh sách bạn bè hiện tại
        List<UserEntity> currentFriends = getFriends(username);

        // 2️⃣ Lấy tất cả bạn của bạn bè (friend-of-friend)
        Set<UserEntity> friendOfFriends = new HashSet<>();
        for (UserEntity friend : currentFriends) {
            List<UserEntity> fof = getFriends(friend.getUsername());
            friendOfFriends.addAll(fof);
        }

        // Loại bỏ chính mình và bạn bè hiện tại
        friendOfFriends.remove(currentUserEntity);
        currentFriends.forEach(friendOfFriends::remove);

        // 3️⃣ Nếu có bạn chung → ưu tiên họ
        if (!friendOfFriends.isEmpty()) {
            return new ArrayList<>(friendOfFriends);
        }

        // 4️⃣ Nếu không có bạn chung → random user khác
        List<UserEntity> allUserEntities = userRepo.findAll();
        allUserEntities.remove(currentUserEntity);
        allUserEntities.removeAll(currentFriends);

        Collections.shuffle(allUserEntities);
        return allUserEntities.stream().limit(5).collect(Collectors.toList());
    }

}
