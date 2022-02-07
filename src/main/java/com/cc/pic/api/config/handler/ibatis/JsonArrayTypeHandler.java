package com.cc.pic.api.config.handler.ibatis;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;

/**
 * 数据库中json数组字串 与 java数组 互转
 * <p>
 * 主要是用于对象数据 基础类型包装对象不建议用
 */
@Slf4j
@MappedTypes({
        // 内置，基础类型
        Integer[].class,
        Double[].class,
        String[].class,
        BigDecimal[].class,
        // 复杂类型
})
@MappedJdbcTypes(value = {JdbcType.VARCHAR}, includeNullJdbcType = true)
public class JsonArrayTypeHandler<T> extends BaseTypeHandler<T[]> {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String STRING_JSON_ARRAY_EMPTY = "[]";

    static {
        // 未知字段忽略
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 不使用科学计数
        MAPPER.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        // null 值不输出(节省内存)
        MAPPER.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
    }

    private final Class<T[]> type;

    public JsonArrayTypeHandler(Class<T[]> type) {
        Objects.requireNonNull(type);
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T[] parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, toJson(parameter));
    }

    @Override
    public T[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return toObject(rs.getString(columnName), type);
    }

    @Override
    public T[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return toObject(rs.getString(columnIndex), type);
    }

    @Override
    public T[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return toObject(cs.getString(columnIndex), type);
    }

    /**
     * object 转 json
     *
     * @param obj 对象
     * @return String json字符串
     */
    private String toJson(T[] obj) {
        if (ArrayUtils.isEmpty(obj)) {
            return STRING_JSON_ARRAY_EMPTY;
        }

        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("mybatis column to json error,obj:" + Arrays.toString(obj), e);
        }
    }

    /**
     * 转换对象
     *
     * @param json  json数据
     * @param clazz 类
     * @return E
     */
    private T[] toObject(String json, Class<T[]> clazz) {
        if (json == null) {
            return null;
        }

        if (StringUtils.isBlank(json)) {
            return newArray(clazz);
        }

        try {
            return MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            log.error("mybatis column json to object error,json:{}", json, e);
            return newArray(clazz);
        }
    }

    private T[] newArray(Class<T[]> clazz) {
        return (T[]) Array.newInstance(clazz.getComponentType(), 0);
    }
}
