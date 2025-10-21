package com.cuongph.be_code.repo;

import com.cuongph.be_code.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    // Tìm tất cả các mối quan hệ bạn bè đã được chấp nhận của 1 user
    @Query("SELECT f FROM Friend f WHERE f.status = 'ACCEPTED' AND (f.requester.id = :userId OR f.receiver.id = :userId)")
    List<Friend> findAcceptedFriends(@Param("userId") Long userId);

    // Kiểm tra quan hệ giữa 2 người (bất kỳ trạng thái nào)
    @Query("SELECT f FROM Friend f WHERE (f.requester.id = :user1 AND f.receiver.id = :user2) OR (f.requester.id = :user2 AND f.receiver.id = :user1)")
    Optional<Friend> findRelation(@Param("user1") Long user1, @Param("user2") Long user2);
}
