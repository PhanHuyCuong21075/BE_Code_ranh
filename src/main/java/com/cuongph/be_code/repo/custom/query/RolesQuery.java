package com.cuongph.be_code.repo.custom.query;

public class RolesQuery {
    protected String sqlRoleByUserName() {
        StringBuilder sql = new StringBuilder("select r.code");
        sql.append(" from roles r");
        sql.append(" JOIN user_role ur ON r.id = ur.role_id");
        sql.append(" where ur.user_id =:userId");

        return sql.toString();
    }
}
