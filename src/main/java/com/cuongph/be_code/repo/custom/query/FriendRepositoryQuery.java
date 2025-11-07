package com.cuongph.be_code.repo.custom.query;

public class FriendRepositoryQuery {


    protected String sqlFindAcceptedFriends() {
        StringBuilder sql = new StringBuilder();

        // Lấy tất cả các bản ghi trong bảng friends (đã được chấp nhận)
        sql.append("SELECT f.* ")
                .append("FROM friends f ")
                .append("WHERE f.status = 'ACCEPTED' ")
                .append("AND (f.requester_id = :userId OR f.receiver_id = :userId)");

        return sql.toString();
    }
    protected String sqlFindFriendship() {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT f.* ")
                .append("FROM friends f ")
                .append("WHERE (f.requester_id = :userId1 AND f.receiver_id = :userId2) ")
                .append("   OR (f.requester_id = :userId2 AND f.receiver_id = :userId1)");

        return sql.toString();
    }

}
