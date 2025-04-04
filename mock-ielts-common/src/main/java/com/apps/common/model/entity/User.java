package com.apps.common.model.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 对应数据库表 COL_Users
 */
@Data
public class User {
    /**
     * 用户ID，对应数据库字段 user_id
     */
    private Integer userId;
    
    /**
     * 用户名，对应数据库字段 username
     */
    private String username;
    
    /**
     * 密码，对应数据库字段 password
     */
    private String password;
    
    /**
     * 电子邮件，对应数据库字段 email
     */
    private String email;
    
    /**
     * 用户角色，对应数据库字段 role
     */
    private String role;
    
    /**
     * 创建时间，对应数据库字段 created_at
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间，对应数据库字段 updated_at
     */
    private LocalDateTime updatedAt;
    
    /**
     * 是否删除，对应数据库字段 deleted
     */
    private Boolean deleted;
} 