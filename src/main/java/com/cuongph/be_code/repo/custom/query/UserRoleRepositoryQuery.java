package com.cuongph.be_code.repo.custom.query;

public class UserRoleRepositoryQuery {
    // Câu query cơ bản để lấy role theo userId
    protected String sqlFindByUserId() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM user_role ");
        sql.append("WHERE user_id = :userId");
        return sql.toString();
    }
}
