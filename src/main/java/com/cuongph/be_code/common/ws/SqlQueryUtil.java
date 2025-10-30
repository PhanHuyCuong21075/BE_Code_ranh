package com.cuongph.be_code.common.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SqlQueryUtil {

    @Autowired
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final ApplicationContext context;

    @Autowired
    public SqlQueryUtil(NamedParameterJdbcTemplate jdbcTemplate, ApplicationContext context) {
        this.jdbcTemplate = jdbcTemplate;
        this.context = context;
    }

    public <T> List<T> queryForList(String sql, Map<String, Object> params, Class<T> clazz) {
        var parameters = new MapSqlParameterSource(params);
        return queryForList(sql, parameters, clazz);
    }

    public <T> List<T> queryForList(String sql, MapSqlParameterSource parameters, Class<T> clazz) {
        if (SqlHelperUtil.isPrimitiveTypeOrWrapper(clazz)) {
            return queryForListPrimitive(sql, parameters, clazz);
        }
        return queryForListNotPrimitive(sql, parameters, clazz);
    }

    private <T> List<T> queryForListPrimitive(String sql, MapSqlParameterSource parameters, Class<T> clazz) {
        return jdbcTemplate.queryForList(sql, parameters, clazz);
    }

    private <T> List<T> queryForListNotPrimitive(String sql, MapSqlParameterSource parameters, Class<T> clazz) {
        return jdbcTemplate.query(sql, parameters, new BeanPropertyRowMapper<>(clazz));
    }

    public <T> T queryForObject(String sql, Map<String, Object> params, Class<T> clazz) {
        var parameters = new MapSqlParameterSource(params);
        return queryForObject(sql, parameters, clazz);
    }

    public <T> T queryForObject(String sql, MapSqlParameterSource parameters, Class<T> clazz) {
        if (SqlHelperUtil.isPrimitiveTypeOrWrapper(clazz)) {
            return queryForObjectPrimitive(sql, parameters, clazz);
        }
        return queryForObjectNotPrimitive(sql, parameters, clazz);
    }

    @SuppressWarnings("unchecked")
    private <T> T queryForObjectPrimitive(String sql, MapSqlParameterSource parameters, Class<T> clazz) {
        var result = queryForListPrimitive(sql, parameters, clazz);
        if (result.isEmpty()) {
            return (T) SqlHelperUtil.getDefaultValue(clazz);
        } else {
            return result.get(0);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T queryForObjectNotPrimitive(String sql, MapSqlParameterSource parameters, Class<T> clazz) {
        var result = queryForListNotPrimitive(sql, parameters, clazz);
        if (result.isEmpty()) {
            return (T) SqlHelperUtil.getDefaultValue(clazz);
        } else {
            return result.get(0);
        }
    }

}
