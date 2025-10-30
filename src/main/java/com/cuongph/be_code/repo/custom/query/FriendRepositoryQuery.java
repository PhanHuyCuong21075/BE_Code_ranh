package com.cuongph.be_code.repo.custom.query;

public class FriendRepositoryQuery {

    // Query lấy danh sách bạn bè đã chấp nhận
    protected String sqlFindAcceptedFriends() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT f.* FROM friend f ");
        sql.append("WHERE f.status = 'ACCEPTED' ");
        sql.append("AND (f.requester_id = :userId OR f.receiver_id = :userId)");
        return sql.toString();
    }

    // Query kiểm tra mối quan hệ giữa 2 user
    protected String sqlFindRelation() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT f.* FROM friend f ");
        sql.append("WHERE (f.requester_id = :user1 AND f.receiver_id = :user2) ");
        sql.append("   OR (f.requester_id = :user2 AND f.receiver_id = :user1)");
        return sql.toString();
    }

    // Query lấy danh sách bạn bè bằng username (JOIN với bảng user)
    protected String sqlFindFriendsByUsername() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CASE WHEN f.requester_id = u.id THEN ur.* ELSE uq.* END ");
        sql.append("FROM friend f ");
        sql.append("JOIN users u ON (f.requester_id = u.id OR f.receiver_id = u.id) ");
        sql.append("JOIN users ur ON ur.id = f.receiver_id ");
        sql.append("JOIN users uq ON uq.id = f.requester_id ");
        sql.append("WHERE f.status = 'ACCEPTED' ");
        sql.append("AND (u.user_name = :username)");
        return sql.toString();
    }

}
