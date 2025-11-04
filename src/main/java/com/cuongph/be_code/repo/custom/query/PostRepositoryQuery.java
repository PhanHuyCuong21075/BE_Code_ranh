package com.cuongph.be_code.repo.custom.query;

public class PostRepositoryQuery {
    // Query lấy bài viết của bạn bè (isPublic = 1) hoặc của chính user
    protected String sqlFindPostsByUserIds() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.* FROM post p ");
        sql.append("WHERE (p.user_id IN (:userIds) AND p.is_public = 1) ");
        sql.append("   OR (p.user_id = :userId) ");
        sql.append("ORDER BY p.created_at DESC");
        return sql.toString();
    }

    // Query lấy tất cả bài public + bài của chính user
    protected String sqlFindAllPublicPostsAndUser() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.* FROM post p ");
        sql.append("WHERE p.is_public = 1 OR p.user_id = :userId ");
        sql.append("ORDER BY p.created_at DESC");
        return sql.toString();
    }
}
