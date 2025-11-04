package com.cuongph.be_code.repo.custom.query;

public class RolesQuery {
    protected String sqlRoleByUserName() {
        StringBuilder sql = new StringBuilder("select r.code");
        sql.append(" from roles r");
        sql.append(" left join user_role ur on r.CODE = ur.role_code");
        sql.append(" where ur.user_name =:userName");

        return sql.toString();
    }
}
