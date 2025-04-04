package com.apps.common.mapper;

import com.apps.common.model.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

/**
 * 用户Mapper接口
 * 使用@Results注解进行字段映射，确保与数据库表字段正确对应
 */
@Mapper
public interface UserMapper {
    
    /**
     * 根据用户名查询用户信息
     * 使用@Results注解进行字段映射，确保与数据库表字段正确对应
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Select("SELECT * FROM COL_Users WHERE username = #{username} AND deleted = 0")
    @Results({
        @Result(property = "userId", column = "user_id"),
        @Result(property = "username", column = "username"),
        @Result(property = "password", column = "password"),
        @Result(property = "email", column = "email"),
        @Result(property = "role", column = "role"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at"),
        @Result(property = "deleted", column = "deleted")
    })
    User selectByUsername(@Param("username") String username);
} 