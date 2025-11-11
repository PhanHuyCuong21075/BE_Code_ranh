package com.cuongph.be_code.service.impl;

import com.cuongph.be_code.common.component.UsersContext;
import com.cuongph.be_code.dto.response.PendingFriendResponse;
import com.cuongph.be_code.entity.FriendEntity;
import com.cuongph.be_code.entity.UserEntity;
import com.cuongph.be_code.repo.FriendRepository;
import com.cuongph.be_code.repo.UserRepository;
import com.cuongph.be_code.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final UsersContext usersContext;

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

        UserEntity currentUser = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));

        // 1️⃣ Query lấy danh sách user gợi ý (loại bỏ bản thân + pending + accepted)
        List<UserEntity> suggested = userRepo.findAllBySuggestedFriends(currentUser.getId());

        // 2️⃣ Trộn lên cho tự nhiên, nếu muốn giới hạn 5 người
        Collections.shuffle(suggested);

        return suggested.stream()
                .limit(5)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public String processRequestFriend(Long otherUserId) {

        Long currentUserId = usersContext.getCurrentUserId();

        if (currentUserId.equals(otherUserId)) {
            throw new RuntimeException("Bạn không thể tự tương tác với chính mình.");
        }
        Optional<FriendEntity> friendshipOpt = friendRepo.findFriendship(currentUserId, otherUserId);

        if (friendshipOpt.isPresent()) {
            FriendEntity friendship = friendshipOpt.get();
            String status = friendship.getStatus();

            switch (status) {
                case "PENDING":
                    if (friendship.getRequesterId().equals(currentUserId)) {
                        friendRepo.delete(friendship);
                        return "Đã hủy lời mời kết bạn.";
                    } else {

                        friendship.setStatus("ACCEPTED");
                        friendship.setAcceptedAt(LocalDateTime.now());
                        friendRepo.save(friendship);
                        return "Đã chấp nhận lời mời.";
                    }

                case "ACCEPTED":
                    friendRepo.delete(friendship);
                    return "Đã hủy kết bạn.";

                case "BLOCKED":
                    throw new RuntimeException("Không thể tương tác do đang bị chặn.");

                default:
                    friendRepo.delete(friendship);
                    break;
            }
        }

        FriendEntity newRequest = new FriendEntity();
        newRequest.setRequesterId(currentUserId);
        newRequest.setReceiverId(otherUserId);
        newRequest.setStatus("PENDING");

        friendRepo.save(newRequest);
        return "Đã gửi lời mời kết bạn.";
    }

    @Override
    public List<PendingFriendResponse> getPendingRequest(String username) {
        UserEntity currentUser = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));

        Long currentUserId = currentUser.getId();

        // Lời mời mà mình gửi đi
        List<FriendEntity> sentRequests = friendRepo.findAllByRequesterIdAndStatus(currentUserId, "PENDING");

        // Lời mời mình nhận
        List<FriendEntity> receivedRequests = friendRepo.findAllByReceiverIdAndStatus(currentUserId, "PENDING");

        List<PendingFriendResponse> result = new ArrayList<>();

        // Gộp: SENT REQUEST
        for (FriendEntity fr : sentRequests) {
            UserEntity user = userRepo.findById(fr.getReceiverId()).orElse(null);
            if (user != null) {
                PendingFriendResponse dto = new PendingFriendResponse();
                dto.setUser(user);
                dto.setType("SENT");
                result.add(dto);
            }
        }

        // Gộp: RECEIVED REQUEST
        for (FriendEntity fr : receivedRequests) {
            UserEntity user = userRepo.findById(fr.getRequesterId()).orElse(null);
            if (user != null) {
                PendingFriendResponse dto = new PendingFriendResponse();
                dto.setUser(user);
                dto.setType("RECEIVED");
                result.add(dto);
            }
        }

        return result;
    }
}
