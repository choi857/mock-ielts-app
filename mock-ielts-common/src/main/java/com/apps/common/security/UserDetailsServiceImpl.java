package com.apps.common.security;

import com.apps.common.exception.AuthException;
import com.apps.common.exception.ErrorCode;
import com.apps.common.mapper.UserMapper;
import com.apps.common.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * UserDetailsService实现类
 * 用于加载用户信息，将数据库中的用户信息转换为Spring Security可用的UserDetails对象
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            // 从数据库加载用户信息
            User user = userMapper.selectByUsername(username);
            if (user == null) {
                log.error("用户不存在：username={}", username);
                throw new AuthException(ErrorCode.USER_NOT_FOUND);
            }

            // 将用户角色转换为Spring Security的GrantedAuthority
            return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
            );
        } catch (AuthException e) {
            throw e;
        } catch (Exception e) {
            log.error("加载用户信息失败：username={}, 原因={}", username, e.getMessage());
            throw new AuthException(ErrorCode.SYSTEM_ERROR, "加载用户信息时发生异常：" + e.getMessage());
        }
    }
} 